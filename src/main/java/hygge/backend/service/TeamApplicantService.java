package hygge.backend.service;

import hygge.backend.dto.ApplicantDto;
import hygge.backend.dto.request.teamapplicant.ApplyRequest;
import hygge.backend.dto.response.teamapplicant.ApplyResponse;
import hygge.backend.dto.response.teamapplicant.GetApplicantsResponse;
import hygge.backend.entity.Member;
import hygge.backend.entity.Team;
import hygge.backend.entity.TeamApplicant;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.TeamApplicantRepository;
import hygge.backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamApplicantService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamApplicantRepository teamApplicantRepository;

    @Transactional(readOnly = true)
    public GetApplicantsResponse getApplicants(Long memberId, Long teamId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new BusinessException("요청하신 팀이 존재하지 않습니다."));

        if(member.getId() != team.getLeader().getId()) throw new BusinessException("요청할 권한이 없습니다.");

        List<ApplicantDto> applicants = team.getTeamApplicants()
                .stream().map(teamApplicant -> new ApplicantDto(teamApplicant.getApplicant())).collect(Collectors.toList());

        return GetApplicantsResponse.builder().applicants(applicants).build();
    }

    @Transactional
    public ApplyResponse apply(Long memberId, ApplyRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(()-> new BusinessException("요청하신 팀이 존재하지 않습니다."));

        // 해당 과목에 이미 소속된 팀이 있는지 검증
        List<Team> belongTeams = member.getMemberTeams().stream().map(memberTeam -> memberTeam.getTeam()).collect(Collectors.toList());

        for (Team belongTeam : belongTeams) {
            if(belongTeam.getSubject().getId() == team.getSubject().getId())
                throw new BusinessException("요청하신 과목에 이미 소속된 팀이 있습니다.");
        }

        TeamApplicant teamApplicant = TeamApplicant.builder()
                                                .applicant(member)
                                                .team(team)
                                                .build();

        TeamApplicant savedTeamApplicant = teamApplicantRepository.save(teamApplicant);

        return new ApplyResponse(savedTeamApplicant);
    }
}
