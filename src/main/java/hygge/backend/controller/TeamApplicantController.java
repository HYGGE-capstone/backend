package hygge.backend.controller;

import hygge.backend.dto.ApplyResultDto;
import hygge.backend.dto.request.teamapplicant.ApplyRequest;
import hygge.backend.dto.request.teamapplicant.ApplyResultRequestDto;
import hygge.backend.dto.response.teamapplicant.ApplyResponse;
import hygge.backend.dto.response.teamapplicant.GetApplicantsResponse;
import hygge.backend.service.TeamApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/applicant")
@RequiredArgsConstructor
public class TeamApplicantController {
    private final TeamApplicantService teamApplicantService;

    // 팀에 대한 모든 지원자 조회 GET 팀장
    @GetMapping
    public ResponseEntity<GetApplicantsResponse> getApplicants(Principal principal, @RequestParam Long teamId) {
        Long memberId = Long.parseLong(principal.getName());
        GetApplicantsResponse response = teamApplicantService.getApplicants(memberId, teamId);
        return ResponseEntity.ok(response);
    }

    // 팀에 지원자가 지원 POST
    @PostMapping
    public ResponseEntity<ApplyResponse> apply(Principal principal, @RequestBody ApplyRequest request) {
        Long memberId = Long.parseLong(principal.getName());
        ApplyResponse response = teamApplicantService.apply(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 지원자의 지원을 수락 POST
    @PostMapping("/accept")
    public ResponseEntity<ApplyResultDto> applyAccept(Principal principal, @RequestBody ApplyResultRequestDto request) {
        Long memberId = Long.parseLong(principal.getName());
        ApplyResultDto response = teamApplicantService.applyAccept(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 지원자의 지원을 거절 POST
    @PostMapping("/reject")
    public ResponseEntity<ApplyResultDto> applyReject(Principal principal, @RequestBody ApplyResultRequestDto request) {
        Long memberId = Long.parseLong(principal.getName());
        ApplyResultDto response = teamApplicantService.applyReject(memberId, request);
        return ResponseEntity.ok(response);
    }
}
