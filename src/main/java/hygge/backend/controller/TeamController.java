package hygge.backend.controller;

import hygge.backend.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "팀", description = "팀 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    //팀 생성 API
    public ResponseEntity<?> createTeam(Principal principal, @RequestBody CreateTeamRequest request) {
        Long memberId = Long.parseLong(principal.getName());
        CreateTeamResponse response = teamService.createTeam(memberId, request);
        return ResponseEntity.ok(response);
    }

}
