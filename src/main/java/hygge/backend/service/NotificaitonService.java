package hygge.backend.service;

import hygge.backend.entity.Member;
import hygge.backend.entity.Notification;
import hygge.backend.entity.NotificationCase;
import hygge.backend.repository.NotificationRepository;
import hygge.backend.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificaitonService {

    private final SubscribeRepository subscribeRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendNotification(NotificationCase notiCase, Long id, List<String> info) {

        switch (notiCase) {

            case NEW_TEAM:    // info[0] = 팀 이름, info[1] = 과목 이름 , info[2] = 과목 코드
                Long subjectId = id;
                String teamName = info.get(0);
                String from = info.get(1) + "(" + info.get(2) + ")";  // 과목 이름(과목 코드)
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
                break;

            case NEW_APPLICANT:
                break;
            case NEW_SUBSCRIBER:
                break;
            case NEW_OFFER_RESULT:
                break;
            case NEW_TEAM_MEMBER:
                break;

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
