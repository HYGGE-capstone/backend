package hygge.backend.controller;

import hygge.backend.dto.response.notification.NotificationListDto;
import hygge.backend.service.NotificaitonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificaitonService notificaitonService;

    @GetMapping
    public NotificationListDto getNotifications(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return notificaitonService.getNotifications(memberId);
    }
}
