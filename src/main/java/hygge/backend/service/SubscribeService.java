package hygge.backend.service;

import hygge.backend.dto.SubscribeDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.Subject;
import hygge.backend.entity.Subscribe;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.SubjectRepository;
import hygge.backend.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public SubscribeDto subscribe(SubscribeDto subscribeDto) {
        Member member = memberRepository.findById(subscribeDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));
        Subject subject = subjectRepository.findById(subscribeDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("해당하는 과목이 존재하지 않습니다."));

        if(subscribeRepository.existsByMemberIdAndSubjectId(member.getId(), subject.getId())){
            throw new RuntimeException("이미 구독한 과목입니다.");
        }

        Subscribe subscribe = Subscribe.builder()
                                .member(member)
                                .subject(subject)
                                .build();

        subscribeRepository.save(subscribe);

        return subscribeDto;
    }

    @Transactional
    public SubscribeDto unsubscribe(SubscribeDto subscribeDto) {
        Member member = memberRepository.findById(subscribeDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));
        Subject subject = subjectRepository.findById(subscribeDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("해당하는 과목이 존재하지 않습니다."));

        Subscribe subscribe = subscribeRepository.findByMemberIdAndSubjectId(member.getId(), subject.getId())
                .orElseThrow(() -> new RuntimeException("구독되지 않은 과목입니다."));

        subscribeRepository.delete(subscribe);

        return subscribeDto;
    }

}
