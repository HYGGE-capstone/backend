package hygge.backend.controller;

import hygge.backend.dto.ResumeDto;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.SignupResponse;
import hygge.backend.entity.Resume;
import hygge.backend.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "과목에 해당하는 멤버 이력서 조회 메서드", description = "과목에 해당하는 멤버 이력서 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResumeDto.class))),
            @ApiResponse(responseCode = "400", description = "이력서 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/subject/{subjectId}/member/{memberId}")
    public ResponseEntity<ResumeDto> getResumeBySubjectAndMember(@PathVariable Long subjectId, @PathVariable Long memberId) {
        ResumeDto response = resumeService.getResumeBySubjectAndMember(subjectId, memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과목에 해당하는 내 이력서 조회 메서드", description = "과목에 해당하는 내 이력서 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResumeDto.class))),
            @ApiResponse(responseCode = "400", description = "이력서 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/subject/{subjectId}/me")
    public ResponseEntity<ResumeDto> getMyResumeBySubject(@PathVariable Long subjectId, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        ResumeDto response = resumeService.getResumeBySubjectAndMember(subjectId, memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이력서 수정 메서드", description = "이력서 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResumeDto.class))),
            @ApiResponse(responseCode = "400", description = "이력서 수정 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PutMapping
    public ResumeDto fixResume(Principal principal, @RequestBody ResumeDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return resumeService.fixResume(memberId, request);
    }
}
