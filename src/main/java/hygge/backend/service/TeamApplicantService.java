package hygge.backend.service;

import hygge.backend.dto.ApplicantDto;
import hygge.backend.dto.ApplyResultDto;
import hygge.backend.dto.notification.NewApplicantNotiDto;
import hygge.backend.dto.notification.NewOfferResultNotiDto;
import hygge.backend.dto.notification.NewTeamMemberNotiDto;
import hygge.backend.dto.request.teamapplicant.ApplyRequest;
import hygge.backend.dto.request.teamapplicant.ApplyResultRequestDto;
import hygge.backend.dto.response.teamapplicant.ApplyResponse;
import hygge.backend.dto.response.teamapplicant.GetApplicantsResponse;
import hygge.backend.entity.*;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.MemberTeamRepository;
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
    private final MemberTeamRepository memberTeamRepository;

    private final NotificaitonService notificaitonService;

    @Transactional(readOnly = true)
    public GetApplicantsResponse getApplicants(Long memberId, Long teamId) {
        log.info("TeamApplicantService.getApplicants");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new BusinessException("요청하신 팀이 존재하지 않습니다."));

        if(member.getId() != team.getLeader().getId()) throw new BusinessException("요청할 권한이 없습니다.");

        List<ApplicantDto> applicants = team.getTeamApplicants()
                .stream().map(teamApplicant -> new ApplicantDto(teamApplicant.getApplicant(), teamApplicant.getId())).collect(Collectors.toList());

        return GetApplicantsResponse.builder().applicants(applicants).build();
    }

    @Transactional
    public ApplyResponse apply(Long memberId, ApplyRequest request) {
        log.info("TeamApplicantService.apply");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(()-> new BusinessException("요청하신 팀이 존재하지 않습니다."));

        if (teamApplicantRepository.existsByTeamIdAndApplicantId(team.getId(), member.getId())) {
            throw new BusinessException("이미 해당 팀에 지원하였습니다.");
        }

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

        // 새로운 지원자 알림
        NewApplicantNotiDto newApplicantNotiDto = NewApplicantNotiDto.builder()
                .applicantLoginId(member.getLoginId())
                .applicantNickname(member.getNickname())
                .teamName(team.getName())
                .leaderId(team.getLeader().getId())
                .build();

        notificaitonService.sendNotification(NotificationCase.NEW_APPLICANT, newApplicantNotiDto);

        return new ApplyResponse(savedTeamApplicant);
    }

    @Transactional
    public ApplyResultDto applyAccept(Long memberId, ApplyResultRequestDto request) {
        log.info("TeamApplicantService.applyAccept");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        TeamApplicant teamApplicant = teamApplicantRepository.findById(request.getTeamApplicantId())
                .orElseThrow(() -> new BusinessException("요청하신 팀 지원을 찾을 수 없습니다."));

        Team team = teamApplicant.getTeam();

        Member applicant = teamApplicant.getApplicant();

        if(member.getId() != team.getLeader().getId()) throw new BusinessException("요청할 권한이 없습니다.");

        if(team.getNumMember() + 1 >= team.getMaxMember()) throw new BusinessException("더 이상 팀원을 받을 수 없습니다.");

        MemberTeam memberTeam = MemberTeam.builder().team(team).member(applicant).build();
        memberTeamRepository.save(memberTeam);

        team.joinMember();
        teamRepository.save(team);

        teamApplicantRepository.delete(teamApplicant);

        // 새로운 팀원 알림
        NewTeamMemberNotiDto newTeamMemberNotiDto = NewTeamMemberNotiDto.builder()
                .memberLoginId(applicant.getLoginId())
                .memberNickname(applicant.getNickname())
                .teamId(team.getId())
                .teamName(team.getName())
                .build();

        notificaitonService.sendNotification(NotificationCase.NEW_TEAM_MEMBER, newTeamMemberNotiDto);

        return ApplyResultDto.builder()
                .applicantId(applicant.getId())
                .teamId(team.getId())
                .result("ACCEPT")
                .build();
    }

    @Transactional
    public ApplyResultDto applyReject(Long memberId, ApplyResultRequestDto request) {
        log.info("TeamApplicantService.applyReject");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        TeamApplicant teamApplicant = teamApplicantRepository.findById(request.getTeamApplicantId())
                .orElseThrow(() -> new BusinessException("요청하신 팀 지원을 찾을 수 없습니다."));

        Team team = teamApplicant.getTeam();

        Member applicant = teamApplicant.getApplicant();

        if(member.getId() != team.getLeader().getId()) throw new BusinessException("요청할 권한이 없습니다.");

        teamApplicantRepository.delete(teamApplicant);

        return ApplyResultDto.builder()
                .applicantId(applicant.getId())
                .teamId(team.getId())
                .result("REJECT")
                .build();
    }
}
