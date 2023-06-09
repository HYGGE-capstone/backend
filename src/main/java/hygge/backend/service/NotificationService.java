package hygge.backend.service;

import hygge.backend.dto.notification.NotificationDto;
import hygge.backend.dto.notification.*;
import hygge.backend.dto.notification.NotiDirtyCheck;
import hygge.backend.dto.notification.NotificationListDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.Notification;
import hygge.backend.entity.NotificationCase;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static hygge.backend.error.exception.ExceptionInfo.CANNOT_FIND_MEMBER;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final NotificationRepository notificationRepository;

    private final MemberTeamRepository memberTeamRepository;

    // 새로운 팀 생성 알림
    public void sendNotification(NotificationCase notiCase, NewTeamNotiDto newTeamNotiDto) {

        log.info("{} NOTIFICATION SEND", notiCase);
        Long subjectId = newTeamNotiDto.getSubjectId();
        String teamName = newTeamNotiDto.getTeamName();
        String from = newTeamNotiDto.getSubjectName() + "(" + newTeamNotiDto.getSubjectCode() + ")";  // 과목 이름(과목 코드)
        String content = newTeamContent(teamName);
        List<Member> subscribers = subscribeRepository.findBySubjectId(subjectId)
                .stream().map(subscribe -> subscribe.getMember()).collect(Collectors.toList());

        for (Member subscriber : subscribers) {
            Notification notification = Notification.builder()
                    .from(from)
                    .content(content)
                    .to(subscriber)
                    .isOpened(false)
                    .build();

            notificationRepository.save(notification);
        }
    }

    // 팀 합류 제안 알림
    public void sendNotification(OfferNotiDto offerNotiDto) {
        Member to = memberRepository.findById(offerNotiDto.getTo())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        Notification notification = Notification.builder()
                .to(to)
                .from(offerNotiDto.getFrom())
                .content(offerNotiDto.getMsg())
                .isOpened(false)
                .build();

        notificationRepository.save(notification);
    }

    // 팀 추방 알림
    public void sendNotification(KickOutNotiDto kickOutNotiDto) {
        Member to = memberRepository.findById(kickOutNotiDto.getTo())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        Notification notification = Notification.builder()
                .to(to)
                .from(kickOutNotiDto.getFrom())
                .content(kickOutNotiDto.getMsg())
                .isOpened(false)
                .build();

        notificationRepository.save(notification);
    }

    // 새로운 구독자 알림
    public void sendNotification(NotificationCase notiCase, NewSubscriberNotiDto newSubscriberNotiDto) {

        log.info("{} NOTIFICATION SEND", notiCase);
        Long subjectId = newSubscriberNotiDto.getSubjectId();
        String from =
                newSubscriberNotiDto.getSubjectName() + "(" + newSubscriberNotiDto.getSubjectCode() + ")";  // 과목 이름(과목 코드)
        String content =
                newSubscriberContent(newSubscriberNotiDto.getSubscriberNickname(), newSubscriberNotiDto.getSubscriberLoginId());
        List<Member> leaders = teamRepository.findBySubjectId(subjectId)
                .stream().map(team -> team.getLeader()).collect(Collectors.toList());

        for (Member leader : leaders) {
            Notification notification = Notification.builder()
                    .from(from)
                    .content(content)
                    .to(leader)
                    .isOpened(false)
                    .build();

            notificationRepository.save(notification);
        }
    }

    // 새로운 지원자 알림
    public void sendNotification(NotificationCase notiCase, NewApplicantNotiDto newApplicantNotiDto){
        log.info("{} NOTIFICATION SEND", notiCase);
        Member leader = memberRepository.findById(newApplicantNotiDto.getLeaderId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        // - from : 팀 이름
        String from = newApplicantNotiDto.getTeamName();

        //  - content : <지원자 닉네임>(<지원자 로그인 아이디>) 님이 팀에 지원하였습니다.
        String content = newApplicantNotiDto.getApplicantNickname() +
                "(" + newApplicantNotiDto.getApplicantLoginId() + ")"
                + " 님이 팀에 지원하였습니다.";

        Notification notification = Notification.builder()
                                        .from(from)
                                        .to(leader)
                                        .content(content)
                                        .isOpened(false)
                                        .build();

        notificationRepository.save(notification);
    }

    // 제안 수락 거절 알림
    public void sendNotification(NotificationCase notiCase, NewOfferResultNotiDto newOfferResultNotiDto) {
        log.info("{} NOTIFICATION SEND", notiCase);
        Member leader = memberRepository.findById(newOfferResultNotiDto.getLeaderId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        // from : 팀 이름
        String from = newOfferResultNotiDto.getTeamName();

        // content : <지원자 닉네임>(<지원자 로그인 아이디>) 님이 제안을 <수락/거절>하였습니다.
        String content = null;
        if (newOfferResultNotiDto.isAccept()) {
            content = newOfferResultNotiDto.getApplicantNickname()
                    + "(" + newOfferResultNotiDto.getApplicantLoginId() + ")"
                    + " 님이 제안을 수락하였습니다.";
        } else {
            content = newOfferResultNotiDto.getApplicantNickname()
                    + "(" + newOfferResultNotiDto.getApplicantLoginId() + ")"
                    + " 님이 제안을 거절하였습니다.";
        }

        Notification notification = Notification.builder()
                .from(from)
                .to(leader)
                .content(content)
                .isOpened(false)
                .build();

        notificationRepository.save(notification);
    }

    // 새로운 팀원 알림
    public void sendNotification(NotificationCase notiCase, NewTeamMemberNotiDto newTeamMemberNotiDto) {
        log.info("{} NOTIFICATION SEND", notiCase);
        String from = newTeamMemberNotiDto.getTeamName();
        String content =
                newTeamMemberContent(newTeamMemberNotiDto.getMemberNickname(),
                        newTeamMemberNotiDto.getMemberLoginId());

        List<Member> members = memberTeamRepository.findByTeamId(newTeamMemberNotiDto.getTeamId()).stream()
                .map(memberTeam -> memberTeam.getMember()).collect(Collectors.toList());

        for (Member member : members) {
            Notification notification = Notification.builder()
                    .from(from)
                    .to(member)
                    .content(content)
                    .isOpened(false)
                    .build();
            notificationRepository.save(notification);
        }

    }

    // 새로운 지원 결과 알림
    public void sendNotification(NotificationCase notiCase, NewApplyResultNotiDto newApplyResultNotiDto) {
        log.info("{} NOTIFICATION SEND", notiCase);
        Member member = memberRepository.findById(newApplyResultNotiDto.getApplicantId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        String from = newApplyResultNotiDto.getTeamName();
        String content = "";
        if(newApplyResultNotiDto.isAccept()){
            content = newApplyResultNotiDto.getTeamName() +
                    "에서 지원을 수락하였습니다.";
        }else{
            content = newApplyResultNotiDto.getTeamName() +
                    "에서 지원을 거절하였습니다.";
        }

        Notification notification = Notification.builder()
                .from(from)
                .to(member)
                .content(content)
                .isOpened(false)
                .build();
        notificationRepository.save(notification);

    }

    // 알림 불러오기 메서드
    public NotificationListDto getNotifications(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        List<NotificationDto> notificationDtoList = member.getNotifications()
                .stream().map(notification -> new NotificationDto(notification)).collect(Collectors.toList());

        for (Notification notification : member.getNotifications()) {
            if(!notification.isOpened()) {
                notification.open();
                notificationRepository.save(notification);
            }
        }

        return NotificationListDto.builder()
                .notifications(notificationDtoList).build();
    }

    @Transactional(readOnly = true)
    public NotiDirtyCheck notiDirtyCheck(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        Comparator<Notification> comparator = new Comparator<Notification>() {
            @Override
            public int compare(Notification o1, Notification o2) {
                return o2.getCreatedTime().compareTo(o1.getCreatedTime());
            }
        };

        List<Notification> notifications = member.getNotifications();
        Collections.sort(notifications, comparator);

        if (!notifications.isEmpty() && !notifications.get(0).isOpened()) {
            return NotiDirtyCheck.builder().isDirty(true).build();
        } else {
            return NotiDirtyCheck.builder().isDirty(false).build();
        }
    }

    public String newTeamContent(String teamName){
        return "새로운 팀 \"" + teamName + "\" 이(가) 생성되었습니다.";
    }

    public String newApplicantContent(String nickname, String loginId) {
        return nickname + "(" + loginId + ")" + " 님이 팀에 지원하였습니다.";
    }

    public String newSubscriberContent(String nickname, String loginId) {
        return nickname + "(" + loginId + ")" + " 님이 과목을 구독하였습니다.";
    }

    public String newOfferResultContent(String nickname, String loginId, boolean isAccepted) {
        if (isAccepted) {
            return nickname + "(" + loginId + ")" + " 님이 제안을 수락하였습니다.";
        } else {
            return nickname + "(" + loginId + ")" + " 님이 제안을 거절하였습니다.";
        }
    }

    public String newTeamMemberContent(String nickname, String loginId) {
        return nickname + "(" + loginId + ")" + " 님이 팀에 합류하였습니다.";
    }
}
