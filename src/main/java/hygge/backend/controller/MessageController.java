package hygge.backend.controller;

import hygge.backend.dto.message.*;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.SignupResponse;
import hygge.backend.entity.MessageRoom;
import hygge.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "쪽지보내기 메서드", description = "쪽지보내기 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쪽지보내기 성공",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "쪽지보내기 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    public MessageDto sendMessage(Principal principal, @RequestBody SendMessageRequest request) {
        Long fromMemberId = Long.parseLong(principal.getName());
        return messageService.sendMessage(fromMemberId, request);
    }

   // 쪽지함 받기
   @Operation(summary = "쪽지함 정보 보기 메서드", description = "쪽지함 정보 보기 메서드입니다.")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "쪽지함 정보 보기 성공",
                   content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageRoomDto.class)))),
           @ApiResponse(responseCode = "400", description = "쪽지함 정보 보기 실패",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

   })
    @GetMapping("/room")
    public List<MessageRoomDto> getMessageRoom(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return messageService.getMessageRoom(memberId);
    }

    // 쪽지함 별 쪽지 받기
    @Operation(summary = "쪽지함 별 쪽지 보기 메서드", description = "쪽지함 별 쪽지 보기 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쪽지함 별 쪽지 보기 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageDto.class)))),
            @ApiResponse(responseCode = "400", description = "쪽지함 별 쪽지 보기 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/room/{roomId}")
    public List<MessageDto> getMessages(Principal principal, @PathVariable Long roomId) {
        Long memberId = Long.parseLong(principal.getName());
        return messageService.getMessages(memberId, roomId);
    }

    // 쪽지함 삭제
    @Operation(summary = "쪽지함 삭제 메서드", description = "쪽지함 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쪽지함 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteMessageRoomDto.class))),
            @ApiResponse(responseCode = "400", description = "쪽지함 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @DeleteMapping("/room/{roomId}")
    public DeleteMessageRoomDto deleteMessageRoom(Principal principal, @PathVariable Long roomId) {
        Long memberId = Long.parseLong(principal.getName());
        return messageService.deleteMessageRoom(memberId, roomId);
    }


    // 메인 화면에서 변경감지
    @Operation(summary = "메인화면에서 변경감지 메서드", description = "메인화면에서 변경감지 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경감지 성공",
                    content = @Content(schema = @Schema(implementation = MessageDirtyCheck.class))),
            @ApiResponse(responseCode = "400", description = "변경감지 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/check/total")
    public MessageDirtyCheck checkTotalDirty(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return messageService.checkTotalDirty(memberId);
    }
}
