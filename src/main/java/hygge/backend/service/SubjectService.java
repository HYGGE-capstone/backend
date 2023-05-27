package hygge.backend.service;

import hygge.backend.dto.SubjectDto;
import hygge.backend.dto.response.subject.SearchSubjectResponse;
import hygge.backend.entity.School;
import hygge.backend.entity.Subject;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.error.exception.ExceptionInfo;
import hygge.backend.repository.SchoolRepository;
import hygge.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;

    public SearchSubjectResponse searchSubjects(String query) {
        List<SubjectDto> subjects = subjectRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query)
                .stream().map(subject -> new SubjectDto(subject)).collect(Collectors.toList());
        return SearchSubjectResponse.builder().subjects(subjects).build();
    }

    public List<SubjectDto> getSubjectsBySchoolId(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_SCHOOL));

        return subjectRepository.findBySchool(school)
                .stream().map(subject -> new SubjectDto(subject)).collect(Collectors.toList());
    }
}
