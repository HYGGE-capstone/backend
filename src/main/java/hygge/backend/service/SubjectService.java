package hygge.backend.service;

import hygge.backend.dto.subject.SubjectDto;
import hygge.backend.dto.response.subject.SearchSubjectResponse;
import hygge.backend.dto.subject.SubjectNoIdDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.School;
import hygge.backend.entity.Subject;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.error.exception.ExceptionInfo;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.SchoolRepository;
import hygge.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public SearchSubjectResponse searchSubjects(String query, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        Long schoolId = member.getSchool().getId();

        List<Subject> subjects = subjectRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query);
        List<SubjectDto> result = new ArrayList<>();

        for (Subject subject : subjects) {
            if (subject.getSchool().getId().equals(schoolId)) {
                result.add(new SubjectDto(subject));
            }
        }

        return SearchSubjectResponse.builder().subjects(result).build();
    }

    @Transactional(readOnly = true)
    public List<SubjectDto> getSubjectsBySchoolId(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_SCHOOL));

        return subjectRepository.findBySchool(school)
                .stream().map(subject -> new SubjectDto(subject)).collect(Collectors.toList());
    }

    @Transactional
    public SubjectDto enrollSubject(SubjectNoIdDto subjectNoIdDto) {
        School school = schoolRepository.findById(subjectNoIdDto.getSchoolId())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_SCHOOL));
        Subject newSubject = subjectNoIdDto.toEntity(school);
        Subject savedSubject = subjectRepository.save(newSubject);

        return new SubjectDto(savedSubject);
    }

    @Transactional
    public SubjectDto fixSubject(SubjectDto subjectDto) {
        Subject subject = subjectRepository.findById(subjectDto.getSubjectId())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_SUBJECT));

        subject.change(subjectDto);
        Subject savedSubject = subjectRepository.save(subject);

        return new SubjectDto(savedSubject);
    }

    @Transactional
    public SubjectDto deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_SUBJECT));

        if (!subject.getTeams().isEmpty()) throw new BusinessException(ExceptionInfo.TEAMS_EXIST);
        subjectRepository.delete(subject);

        return new SubjectDto(subject);
    }
}

