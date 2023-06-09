package hygge.backend.controller;

import hygge.backend.dto.team.*;
import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.service.TeamService;
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

@Tag(name = "회원 - 팀", description = "팀 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성 메서드", description = "팀을 생성하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 생성 성공.",
                    content = @Content(schema = @Schema(implementation = CreateTeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 생성 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<CreateTeamResponse> createTeam(Principal principal, @RequestBody CreateTeamRequest request) {
        Long memberId = Long.parseLong(principal.getName());
        CreateTeamResponse response = teamService.createTeam(memberId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소속된 팀 조회 메서드", description = "소속된 팀을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소속된 팀 조회 성공.",
                    content = @Content(schema = @Schema(implementation = TeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "소속된 팀 조회 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<TeamResponse> getTeams(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        TeamResponse response = teamService.getTeams(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "괴목별 팀 조회 메서드", description = "괴목별 팀을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과목별 팀 조회 성공.",
                    content = @Content(schema = @Schema(implementation = TeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "과목별 팀 조회 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<TeamResponse> searchTeams(@RequestParam Long subjectId) {
        TeamResponse response = teamService.searchTeams(subjectId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀에 속한 멤버 조회 메서드", description = "팀에 속한 멤버들을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공.",
                    content = @Content(schema = @Schema(implementation = GetMembersByTeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/members")
    public ResponseEntity<GetMembersByTeamResponse> getMembersByTeam(@RequestParam Long teamId) {
        GetMembersByTeamResponse response = teamService.getMembersByTeam(teamId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀 미소속 구독자 조회 메서드", description = "팀에 속하지 않은 구독자를 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공.",
                    content = @Content(schema = @Schema(implementation = GetSubscribersNotBelongTeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/subscribers")
    public ResponseEntity<GetSubscribersNotBelongTeamResponse> getSubscribersNotBelongTeam(Principal principal, @RequestParam Long subjectId){
        Long memberId = Long.parseLong(principal.getName());
        GetSubscribersNotBelongTeamResponse response = teamService.getSubscribersNotBelongTeam(memberId, subjectId);
        return ResponseEntity.ok(response);
    }

    // 팀 탈퇴
    @Operation(summary = "팀 탈퇴 메서드", description = "팀 탈퇴 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 탈퇴 성공.",
                    content = @Content(schema = @Schema(implementation = LeaveTeamDto.class))),
            @ApiResponse(responseCode = "400", description = "팀 탈퇴 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/leave")
    public LeaveTeamDto leaveTeam(Principal principal, @RequestBody LeaveTeamDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return teamService.leaveTeam(memberId, request);
    }

    // 팀장 위임
    @Operation(summary = "팀장 위임 메서드", description = "팀장 위임 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀장 위임 성공.",
                    content = @Content(schema = @Schema(implementation = MandateLeaderDto.class))),
            @ApiResponse(responseCode = "400", description = "팀장 위임 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/mandate")
    public MandateLeaderDto mandateLeader(Principal principal, @RequestBody MandateLeaderDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return teamService.mandateLeader(memberId, request);
    }

    // 팀원 추방
    @Operation(summary = "팀원 추방 메서드", description = "팀원 추방 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀원 추방 성공.",
                    content = @Content(schema = @Schema(implementation = KickOutMemberDto.class))),
            @ApiResponse(responseCode = "400", description = "팀원 추방 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/kick-out")
    public KickOutMemberDto kickOutMember(Principal principal, @RequestBody KickOutMemberDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return teamService.kickOutMember(memberId, request);
    }

    // 팀 정보 수정
    @Operation(summary = "팀 정보 수정 메서드", description = "팀 정보 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 정보 수정 성공.",
                    content = @Content(schema = @Schema(implementation = TeamDto.class))),
            @ApiResponse(responseCode = "400", description = "팀 정보 수정 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/update")
    public TeamDto updateTeam(Principal principal, @RequestBody UpdateTeamDto request) {
        Long memberId = Long.parseLong(principal.getName());
        return teamService.updateTeam(memberId, request);
    }

    // 팀 해체
    @Operation(summary = "팀 해체 메서드", description = "팀 해체 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 해체 성공.",
                    content = @Content(schema = @Schema(implementation = TeamDto.class))),
            @ApiResponse(responseCode = "400", description = "팀 해체 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/dissolve")
    public TeamDto dissolveTeam(Principal principal, @RequestParam Long teamId) {
        Long memberId = Long.parseLong(principal.getName());
        return teamService.dissolveTeam(memberId, teamId);
    }

}
