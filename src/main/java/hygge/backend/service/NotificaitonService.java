package hygge.backend.service;

import hygge.backend.dto.notification.NewSubscriberNotiDto;
import hygge.backend.dto.notification.NewTeamNotiDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.Notification;
import hygge.backend.entity.NotificationCase;
import hygge.backend.repository.NotificationRepository;
import hygge.backend.repository.SubscribeRepository;
import hygge.backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificaitonService {

    private final SubscribeRepository subscribeRepository;
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
