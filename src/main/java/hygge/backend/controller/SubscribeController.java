package hygge.backend.controller;

import hygge.backend.dto.SubjectDto;
import hygge.backend.dto.SubscribeDto;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.entity.Subscribe;
import hygge.backend.service.SubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "구독", description = "구독 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    // 구독 메서드
    @Operation(summary = "구독 메서드", description = "사용자가 원하는 과목을 구독합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 성공.",
                    content = @Content(schema = @Schema(implementation = SubscribeDto.class))),
            @ApiResponse(responseCode = "400", description = "구독 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/subscribe")
    public ResponseEntity<SubscribeDto> subscribe(Principal principal, @RequestBody SubjectDto subjectDto) {
        Long memberId = Long.parseLong(principal.getName());
        Long subjectId = subjectDto.getSubjectId();
        return ResponseEntity.ok(subscribeService.subscribe(new SubscribeDto(memberId, subjectId)));
    }

    // 구독 해제 메서드
    @Operation(summary = "구독 해제 메서드", description = "사용자가 원하는 과목의 구독을 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 해제 성공.",
                    content = @Content(schema = @Schema(implementation = SubscribeDto.class))),
            @ApiResponse(responseCode = "400", description = "구독 해제 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/unsubscribe")
    public ResponseEntity<SubscribeDto> unsubscribe(Principal principal, @RequestBody SubjectDto subjectDto) {
        Long memberId = Long.parseLong(principal.getName());
        Long subjectId = subjectDto.getSubjectId();
        return ResponseEntity.ok(subscribeService.unsubscribe(new SubscribeDto(memberId, subjectId)));
    }
}