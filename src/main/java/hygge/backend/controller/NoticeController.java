package hygge.backend.controller;

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
    @PostMapping
    public NoticeDto postNotice(Principal principal, @RequestBody PostNoticeDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.postNotice(memberId, request);
    }

    // 수정
    @PutMapping
    public NoticeDto updateNotice(Principal principal, @RequestBody UpdateNoticeDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.updateNotice(memberId, request);
    }

    // 삭제
    @DeleteMapping
    public NoticeDto deleteNotice(Principal principal, @RequestParam Long noticeId) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.deleteNotice(memberId, noticeId);
    }

    // 조회
    @GetMapping
    public NoticeDto getNotice(Principal principal, @RequestParam Long teamId) {
        Long memberId = Long.parseLong(principal.getName());
        return noticeService.getNotice(memberId, teamId);
    }
}
