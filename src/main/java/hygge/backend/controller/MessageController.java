package hygge.backend.controller;

import hygge.backend.dto.message.MessageDto;
import hygge.backend.dto.message.MessageRoomDto;
import hygge.backend.dto.message.SendMessageRequest;
import hygge.backend.entity.MessageRoom;
import hygge.backend.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    // 쪽지함 받기
    @GetMapping("/room")
    public List<MessageRoomDto> getMessageRoom(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return messageService.getMessageRoom(memberId);
    }

    // 쪽지함 별 쪽지 받기
    @GetMapping("/room/{roomId}")
    public List<MessageDto> getMessages(Principal principal, @PathVariable Long roomId) {
        Long memberId = Long.parseLong(principal.getName());
        return messageService.getMessages(memberId, roomId);
    }

}
