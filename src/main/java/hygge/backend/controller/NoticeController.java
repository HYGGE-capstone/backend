package hygge.backend.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.message.MessageDto;
import hygge.backend.dto.notice.NoticeDto;
import hygge.backend.dto.notice.PostNoticeDto;
import hygge.backend.dto.notice.UpdateNoticeDto;
import hygge.backend.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "회원 - 공지", description = "공지 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 등록
    @Operation(summary = "공지 등록 메서드", description = "공지 등록 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지 등록 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDto.class))),
            @ApiResponse(responseCode = "400", description = "공지 등록 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    public NoticeDto postNotice(Principal principal, @RequestBody PostNoticeDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.postNotice(memberId, request);
    }

    // 수정
    @Operation(summary = "공지 수정 메서드", description = "공지 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지 수정 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDto.class))),
            @ApiResponse(responseCode = "400", description = "공지 수정 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PutMapping
    public NoticeDto updateNotice(Principal principal, @RequestBody UpdateNoticeDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.updateNotice(memberId, request);
    }

    // 삭제
    @Operation(summary = "공지 삭제 메서드", description = "공지 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지 삭제 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDto.class))),
            @ApiResponse(responseCode = "400", description = "공지 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @DeleteMapping
    public NoticeDto deleteNotice(Principal principal, @RequestParam Long noticeId) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.deleteNotice(memberId, noticeId);
    }

    // 조회
    @Operation(summary = "공지 조회 메서드", description = "공지 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지 조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDto.class))),
            @ApiResponse(responseCode = "400", description = "공지 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public NoticeDto getNotice(Principal principal, @RequestParam Long teamId) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.getNotice(memberId, teamId);
    }
}
