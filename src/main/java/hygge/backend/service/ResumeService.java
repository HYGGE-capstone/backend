package hygge.backend.service;

import hygge.backend.dto.ResumeDto;
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

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public ResumeDto postResume(Long memberId, ResumeDto resumeDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("해당하는 멤버를 찾을 수 없습니다."));
        Subject subject = subjectRepository.findById(resumeDto.getSubjectId())
                .orElseThrow(() -> new BusinessException("해당하는 과목을 찾을 수 없습니다."));

        if (resumeRepository.existsByMemberIdAndSubjectId(memberId, subject.getId())) {
            throw new BusinessException("이력서가 이미 존재합니다.");
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
                .orElseThrow(() -> new BusinessException("요청하신 이력서를 찾을 수 없습니다."));

        return new ResumeDto(resume);
    }
}
