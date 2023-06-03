package hygge.backend.controller;

import hygge.backend.dto.notification.NotiDirtyCheck;
import hygge.backend.dto.notification.NotificationListDto;
import hygge.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "회원 - 알림", description = "알림 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificaitonService;

    @GetMapping
    public NotificationListDto getNotifications(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return notificaitonService.getNotifications(memberId);
    }

    @GetMapping("/check")
    public NotiDirtyCheck notiDirtyCheck(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return notificaitonService.notiDirtyCheck(memberId);
    }
}
