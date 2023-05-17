package hygge.backend.service;

import hygge.backend.dto.NotificationDto;
import hygge.backend.dto.notification.NewApplicantNotiDto;
import hygge.backend.dto.notification.NewOfferResultNotiDto;
import hygge.backend.dto.notification.NewSubscriberNotiDto;
import hygge.backend.dto.notification.NewTeamNotiDto;
import hygge.backend.dto.response.notification.NotiDirtyCheck;
import hygge.backend.dto.response.notification.NotificationListDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.Notification;
import hygge.backend.entity.NotificationCase;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.NotificationRepository;
import hygge.backend.repository.SubscribeRepository;
import hygge.backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificaitonService {

    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final NotificationRepository notificationRepository;

    // 새로운 팀 생성 알림
    @Transactional
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

    // 새로운 구독자 알림
    @Transactional
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
    @Transactional
    public void sendNotification(NotificationCase notiCase, NewApplicantNotiDto newApplicantNotiDto){
        log.info("{} NOTIFICATION SEND", notiCase);
        Member leader = memberRepository.findById(newApplicantNotiDto.getLeaderId())
                .orElseThrow(() -> new BusinessException("해당하는 멤버를 찾을 수 없습니다."));

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
    @Transactional
    public void sendNotification(NotificationCase notiCase, NewOfferResultNotiDto newOfferResultNotiDto) {
        log.info("{} NOTIFICATION SEND", notiCase);
        Member leader = memberRepository.findById(newOfferResultNotiDto.getLeaderId())
                .orElseThrow(() -> new BusinessException("해당하는 멤버를 찾을 수 없습니다."));

        // from : 팀 이름
        String from = newOfferResultNotiDto.getTeamName();

        // content : <지원자 닉네임>(<지원자 로그인 아이디>) 님이 제안을 <수락/거절>하였습니다.
        String content = null;
        if (newOfferResultNotiDto.isAccept()) {
            content = newOfferResultNotiDto.getApplicantNickname()
                    + "(" + newOfferResultNotiDto.getApplicantLoginId() + ")"
                    + " 님이 제알을 수락하였습니다.";
        } else {
            content = newOfferResultNotiDto.getApplicantNickname()
                    + "(" + newOfferResultNotiDto.getApplicantLoginId() + ")"
                    + " 님이 제알을 거절하였습니다.";
        }

        Notification notification = Notification.builder()
                .from(from)
                .to(leader)
                .content(content)
                .isOpened(false)
                .build();

        notificationRepository.save(notification);
    }


    // 알림 불러오기 메서드
    @Transactional
    public NotificationListDto getNotifications(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("해당하는 멤버를 찾을 수 없습니다."));

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
                .orElseThrow(() -> new BusinessException("요청하신 멤버를 찾을 수 없습니다."));

        Comparator<Notification> comparator = new Comparator<Notification>() {
            @Override
            public int compare(Notification o1, Notification o2) {
                return o2.getCreatedTime().compareTo(o1.getCreatedTime());
            }
        };

        List<Notification> notifications = member.getNotifications();
        Collections.sort(notifications, comparator);

        log.info(notifications.get(0).getId().toString());

        if (!notifications.get(0).isOpened()) {
            return NotiDirtyCheck.builder().isDirty(true).build();
        } else {
            return NotiDirtyCheck.builder().isDirty(false).build();
        }
    }

    public String newTeamContent(String teamName){
        return "새로운 팀 \"" + teamName + "\" 이 생성되었습니다.";
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
        return nickname + "(" + loginId + ")" + " 님이 팀에 합하였습니다.";
    }
}
