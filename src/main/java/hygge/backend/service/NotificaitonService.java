package hygge.backend.service;

import hygge.backend.entity.NotificationCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificaitonService {

    @Transactional
    public void sendNotification(NotificationCase notiCase, List<Long> memberIds) {


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
