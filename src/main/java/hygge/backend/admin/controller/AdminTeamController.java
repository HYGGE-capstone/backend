package hygge.backend.admin.controller;

import hygge.backend.dto.response.team.TeamResponse;
import hygge.backend.dto.team.TeamDto;
import hygge.backend.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "0 관리자 - 팀", description = "팀(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

    private final TeamService teamService;

    // 과목에 속한 팀 목록 조회
    @GetMapping
    public TeamResponse getTeamList(@RequestParam Long subjectId) {
        return teamService.searchTeams(subjectId);
    }

    // 팀 정보 조회

    // 팀 삭제
    @DeleteMapping
    public TeamDto deleteTeam(@RequestParam Long teamId) {
        return teamService.deleteTeam(teamId);
    }
}
