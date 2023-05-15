package hygge.backend.controller;

import hygge.backend.dto.SubscribeDto;
import hygge.backend.dto.request.team.CreateTeamRequest;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.team.CreateTeamResponse;
import hygge.backend.dto.response.team.TeamResponse;
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

@Tag(name = "팀", description = "팀 관련 API 입니다.")
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
            @ApiResponse(responseCode = "200", description = "팀 조회 성공.",
                    content = @Content(schema = @Schema(implementation = TeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 조회 실패.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<TeamResponse> getTeams(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        TeamResponse response = teamService.getTeams(memberId);
        return ResponseEntity.ok(response);
    }

}
