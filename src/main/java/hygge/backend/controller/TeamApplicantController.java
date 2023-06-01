package hygge.backend.controller;

import hygge.backend.dto.apply.ApplyResultDto;
import hygge.backend.dto.apply.ApplyRequest;
import hygge.backend.dto.apply.ApplyResultRequestDto;
import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.apply.ApplyResponse;
import hygge.backend.dto.apply.GetApplicantsResponse;
import hygge.backend.service.TeamApplicantService;
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

@Tag(name = "회원 - 팀 지원", description = "팀 지원 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/applicant")
@RequiredArgsConstructor
public class TeamApplicantController {
    private final TeamApplicantService teamApplicantService;

    // 팀에 대한 모든 지원자 조회 GET 팀장
    @Operation(summary = "팀에 대한 모든 지원자 조회 메서드", description = "팀에 대한 모든 지원자 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀에 대한 모든 지원자 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetApplicantsResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀에 대한 모든 지원자 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public ResponseEntity<GetApplicantsResponse> getApplicants(Principal principal, @RequestParam Long teamId) {
        Long memberId = Long.parseLong(principal.getName());
        GetApplicantsResponse response = teamApplicantService.getApplicants(memberId, teamId);
        return ResponseEntity.ok(response);
    }

    // 팀에 지원자가 지원 POST
    @Operation(summary = "팀 지원 메서드", description = "팀 지원 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 지원 성공",
                    content = @Content(schema = @Schema(implementation = ApplyResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 지원 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    public ResponseEntity<ApplyResponse> apply(Principal principal, @RequestBody ApplyRequest request) {
        Long memberId = Long.parseLong(principal.getName());
        ApplyResponse response = teamApplicantService.apply(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 지원자의 지원을 수락 POST
    @Operation(summary = "지원자 수락 메서드", description = "지원자 수락 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지원자 수락 성공",
                    content = @Content(schema = @Schema(implementation = ApplyResultDto.class))),
            @ApiResponse(responseCode = "400", description = "지원자 수락 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/accept")
    public ResponseEntity<ApplyResultDto> applyAccept(Principal principal, @RequestBody ApplyResultRequestDto request) {
        Long memberId = Long.parseLong(principal.getName());
        ApplyResultDto response = teamApplicantService.applyAccept(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 지원자의 지원을 거절 POST
    @Operation(summary = "지원자 거절 메서드", description = "지원자 거절 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지원자 거절 성공",
                    content = @Content(schema = @Schema(implementation = ApplyResultDto.class))),
            @ApiResponse(responseCode = "400", description = "지원자 거절 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/reject")
    public ResponseEntity<ApplyResultDto> applyReject(Principal principal, @RequestBody ApplyResultRequestDto request) {
        Long memberId = Long.parseLong(principal.getName());
        ApplyResultDto response = teamApplicantService.applyReject(memberId, request);
        return ResponseEntity.ok(response);
    }
}
