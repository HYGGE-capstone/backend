package hygge.backend.service;

import hygge.backend.dto.request.team.CreateTeamRequest;
import hygge.backend.dto.response.team.CreateTeamResponse;
import hygge.backend.entity.Member;
import hygge.backend.entity.MemberTeam;
import hygge.backend.entity.Subject;
import hygge.backend.entity.Team;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.MemberTeamRepository;
import hygge.backend.repository.SubjectRepository;
import hygge.backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    private final TeamRepository teamRepository;

    private final MemberTeamRepository memberTeamRepository;

    @Transactional
    public CreateTeamResponse createTeam(Long memberId, CreateTeamRequest request) {
        log.info("TeamService.createTeam {}", memberId);

        Member leader = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        log.info("memberRepository.findById = {}", leader.getId());

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new BusinessException("요청하신 과목이 존재하지 않습니다. "));
        log.info("subjectRepository.findById = {}", subject.getId());

        if(request.getMaxMember() < 2) throw new BusinessException("최대 멤버수가 2보다 작습니다.");

        // 유저가 과목에 속한 팀이 이미 있다면 오류 처리
        Long subjectId;

        for(MemberTeam memberTeam : leader.getMemberTeams()){
            subjectId = memberTeam.getTeam().getSubject().getId();
            if (subjectId == request.getSubjectId()) {
                throw new BusinessException("이미 선택된 과목에 팀이 있습니다. ");
            }
        }

        log.info("leader 아이디 {}, subject 아이디 {}", leader.getId(), subject.getId());

        Team team = Team.builder()
                .name(request.getName())
                .title(request.getTitle())
                .description(request.getDescription())
                .subject(subject)
                .leader(leader)
                .maxMember(request.getMaxMember())
                .build();

        Team saveTeam = teamRepository.save(team);

        MemberTeam memberTeam = MemberTeam.builder()
                .member(leader)
                .team(saveTeam)
                .build();

        memberTeamRepository.save(memberTeam);

        CreateTeamResponse response = CreateTeamResponse.builder()
                .teamId(saveTeam.getId())
                .leaderId(leader.getId())
                .name(team.getName())
                .subjectId(subject.getId())
                .title(team.getTitle())
                .description(team.getDescription())
                .maxMember(team.getMaxMember())
                .build();

        return response;
    }
}
