package hygge.backend.controller;

import hygge.backend.dto.SubjectDto;
import hygge.backend.dto.SubscribeDto;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.subscribe.SubscribeResponse;
import hygge.backend.entity.Subscribe;
import hygge.backend.service.SubscribeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "구독", description = "구독 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/v1/subscribe")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Operation(summary = "구독 메서드", description = "사용자가 원하는 과목을 구독합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 성공.",
                    content = @Content(schema = @Schema(implementation = SubscribeDto.class))),
            @ApiResponse(responseCode = "400", description = "구독 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SubscribeDto> subscribe(Principal principal, @RequestBody SubjectDto subjectDto) {
        Long memberId = Long.parseLong(principal.getName());
        Long subjectId = subjectDto.getSubjectId();
        return ResponseEntity.ok(subscribeService.subscribe(new SubscribeDto(memberId, subjectId)));
    }

    @Operation(summary = "구독 해제 메서드", description = "사용자가 원하는 과목의 구독을 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 해제 성공.",
                    content = @Content(schema = @Schema(implementation = SubscribeDto.class))),
            @ApiResponse(responseCode = "400", description = "구독 해제 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/cancel")
    public ResponseEntity<SubscribeDto> unsubscribe(Principal principal, @RequestBody SubjectDto subjectDto) {
        Long memberId = Long.parseLong(principal.getName());
        Long subjectId = subjectDto.getSubjectId();
        return ResponseEntity.ok(subscribeService.unsubscribe(new SubscribeDto(memberId, subjectId)));
    }

    @Operation(summary = "구독한 과목 조회 메서드", description = "구독한 과목을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독한 과목 조회 성공.",
                    content = @Content(schema = @Schema(implementation = SubscribeResponse.class))),
            @ApiResponse(responseCode = "400", description = "구독한 과목 조회 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<SubscribeResponse> getSubscribes(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        SubscribeResponse response = subscribeService.getSubscribes(memberId);
        return ResponseEntity.ok(response);
    }

}
