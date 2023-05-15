package hygge.backend.controller;

import hygge.backend.dto.ResumeDto;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.SignupResponse;
import hygge.backend.service.ResumeService;
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

@Tag(name = "이력서", description = "이력서 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "이력서 등록 메서드", description = "이력서 등록 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResumeDto.class))),
            @ApiResponse(responseCode = "400", description = "이력서 등록 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    public ResponseEntity<ResumeDto> postResume(Principal principal, @RequestBody ResumeDto request) {
        Long memberId = Long.parseLong(principal.getName());

        ResumeDto response = resumeService.postResume(memberId, request);
        return ResponseEntity.ok(response);
    }
}
