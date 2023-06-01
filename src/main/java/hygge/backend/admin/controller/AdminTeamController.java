package hygge.backend.admin.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.subject.SubjectDto;
import hygge.backend.dto.team.TeamResponse;
import hygge.backend.dto.team.TeamDto;
import hygge.backend.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 - 팀", description = "팀(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

    private final TeamService teamService;

    @Operation(summary = "과목에 속한 팀 목록 조회 메서드", description = "과목에 속한 팀 목록 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public TeamResponse getTeamList(@RequestParam Long subjectId) {
        return teamService.searchTeams(subjectId);
    }

    // 팀 삭제
    @Operation(summary = "팀 삭제 메서드", description = "팀 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TeamDto.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public TeamDto deleteTeam(@RequestParam Long teamId) {
        return teamService.deleteTeam(teamId);
    }
}
