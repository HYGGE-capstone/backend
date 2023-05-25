package hygge.backend.controller;

import hygge.backend.dto.message.MessageDto;
import hygge.backend.dto.message.SendMessageRequest;
import hygge.backend.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "쪽지", description = "쪽지 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // 쪽지 보내기
    @PostMapping
    public MessageDto sendMessage(Principal principal, @RequestBody SendMessageRequest request) {
        Long fromMemberId = Long.parseLong(principal.getName());
        return messageService.sendMessage(fromMemberId, request);
    }

}
