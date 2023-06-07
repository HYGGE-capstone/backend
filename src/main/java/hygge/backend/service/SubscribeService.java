package hygge.backend.service;

import hygge.backend.dto.subject.SubjectDto;
import hygge.backend.dto.subscribe.SubscribeDto;
import hygge.backend.dto.notification.NewSubscriberNotiDto;
import hygge.backend.dto.subscribe.SubscribeResponse;
import hygge.backend.entity.*;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.ResumeRepository;
import hygge.backend.repository.SubjectRepository;
import hygge.backend.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static hygge.backend.error.exception.ExceptionInfo.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    private final NotificationService notificaitonService;
    private final ResumeRepository resumeRepository;

    @Transactional
    public SubscribeDto subscribe(SubscribeDto subscribeDto) {
        Member member = memberRepository.findById(subscribeDto.getMemberId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Subject subject = subjectRepository.findById(subscribeDto.getSubjectId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_SUBJECT));

        if(subscribeRepository.existsByMemberIdAndSubjectId(member.getId(), subject.getId())){
            throw new BusinessException(ALREADY_SUBSCRIBE);
        }

        Subscribe subscribe = Subscribe.builder()
                                .member(member)
                                .subject(subject)
                                .build();

        subscribeRepository.save(subscribe);

        // 팀 리더들에게 새로운 구독자 알림 전송
        NewSubscriberNotiDto newSubscriberNotiDto = NewSubscriberNotiDto.builder()
                .subjectId(subject.getId())
                .subjectCode(subject.getCode())
                .subjectName(subject.getName())
                .subscriberNickname(member.getNickname())
                .subscriberLoginId(member.getLoginId())
                .build();
        notificaitonService.sendNotification(NotificationCase.NEW_SUBSCRIBER, newSubscriberNotiDto);

        return subscribeDto;
    }


    @Transactional
    public SubscribeDto unsubscribe(SubscribeDto subscribeDto) {
        Member member = memberRepository.findById(subscribeDto.getMemberId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Subject subject = subjectRepository.findById(subscribeDto.getSubjectId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_SUBJECT));

        Subscribe subscribe = subscribeRepository.findByMemberIdAndSubjectId(member.getId(), subject.getId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_SUBSCRIBE));

        Resume resume = resumeRepository.findBySubjectIdAndMemberId(subject.getId(), member.getId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_RESUME));

        subscribeRepository.delete(subscribe);
        resumeRepository.delete(resume);

        return subscribeDto;
    }

    // 구독한 과목 조회 메서드
    @Transactional(readOnly = true)
    public SubscribeResponse getSubscribes(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        List<SubjectDto> subjects = new ArrayList<>();

        for (Subscribe subscribe : member.getSubscribes()) {
            Subject subject = subscribe.getSubject();

            subjects.add(SubjectDto.builder()
                            .subjectId(subject.getId())
                            .name(subject.getName())
                            .code(subject.getCode())
                            .year(subject.getYear())
                            .semester(subject.getSemester())
                            .professorName(subject.getProfessorName())
                            .time(subject.getTime())
                            .build());
        }

        return SubscribeResponse.builder().subscribes(subjects).build();
    }

}
