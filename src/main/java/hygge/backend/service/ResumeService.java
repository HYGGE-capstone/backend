package hygge.backend.service;

import hygge.backend.dto.resume.ResumeDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.Resume;
import hygge.backend.entity.Subject;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.ResumeRepository;
import hygge.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hygge.backend.error.exception.ExceptionInfo.*;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public ResumeDto postResume(Long memberId, ResumeDto resumeDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Subject subject = subjectRepository.findById(resumeDto.getSubjectId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_SUBJECT));

        if (resumeRepository.existsByMemberIdAndSubjectId(memberId, subject.getId())) {
            throw new BusinessException(ALREADY_HAVE_RESUME);
        }

        Resume resume = Resume.builder()
                .member(member)
                .content(resumeDto.getContent())
                .title(resumeDto.getTitle())
                .subject(subject)
                .build();

        Resume saveResume = resumeRepository.save(resume);

        return new ResumeDto(saveResume);
    }

    @Transactional(readOnly = true)
    public ResumeDto getResumeBySubjectAndMember(Long subjectId, Long memberId) {
        Resume resume = resumeRepository.findBySubjectIdAndMemberId(subjectId, memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_RESUME));

        return new ResumeDto(resume);
    }

    @Transactional
    public ResumeDto fixResume(Long memberId, ResumeDto resumeDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Resume resume = resumeRepository.findById(resumeDto.getResumeId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_RESUME));

        if(!resume.getMember().getId().equals(member.getId())) throw new BusinessException(UNAUTHORIZED_REQUEST);

        resume.changeTitle(resumeDto.getTitle());
        resume.changeContent(resumeDto.getContent());

        Resume savedResume = resumeRepository.save(resume);
        return new ResumeDto(savedResume);
    }
}
