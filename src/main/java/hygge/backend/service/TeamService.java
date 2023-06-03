package hygge.backend.service;

import hygge.backend.dto.team.*;
import hygge.backend.dto.notification.NewTeamNotiDto;
import hygge.backend.entity.*;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hygge.backend.error.exception.ExceptionInfo.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    private final TeamRepository teamRepository;

    private final MemberTeamRepository memberTeamRepository;

    private final NotificaitonService notificaitonService;
    private final SubscribeRepository subscribeRepository;

    public CreateTeamResponse createTeam(Long memberId, CreateTeamRequest request) {
        log.info("TeamService.createTeam {}", memberId);

        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        log.info("memberRepository.findById = {}", leader.getId());

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_SUBJECT));
        log.info("subjectRepository.findById = {}", subject.getId());

        if (request.getMaxMember() < 2) throw new BusinessException(MIN_TEAM_MEMBER);

        // 유저가 과목에 속한 팀이 이미 있다면 오류 처리
        Long subjectId;

        for (MemberTeam memberTeam : leader.getMemberTeams()) {
            subjectId = memberTeam.getTeam().getSubject().getId();
            if (subjectId == request.getSubjectId()) {
                throw new BusinessException(ALREADY_HAVE_TEAM);
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
                .numMember(1)
                .build();

        Team saveTeam = teamRepository.save(team);

        MemberTeam memberTeam = MemberTeam.builder()
                .member(leader)
                .team(saveTeam)
                .build();

        memberTeamRepository.save(memberTeam);

        // 구독자들에게 팀 생성 알림 전송
        NewTeamNotiDto newTeamNotiDto = NewTeamNotiDto.builder()
                .subjectId(subject.getId())
                .teamName(saveTeam.getName())
                .subjectName(subject.getName())
                .subjectCode(subject.getCode())
                .build();
        notificaitonService.sendNotification(NotificationCase.NEW_TEAM, newTeamNotiDto);

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

    @Transactional(readOnly = true)
    public TeamResponse getTeams(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        List<TeamDto> teams = new ArrayList<>();

        for (MemberTeam memberTeam : member.getMemberTeams()) {
            Team team = memberTeam.getTeam();
            Subject subject = team.getSubject();
            boolean isLeader = false;
            if(team.getLeader().getId() == memberId) isLeader = true;

            teams.add(new TeamDto(team, isLeader));
        }
        return TeamResponse.builder().teams(teams).build();
    }

    @Transactional(readOnly = true)
    public TeamResponse searchTeams(Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_SUBJECT));

        List<TeamDto> teams = new ArrayList<>();

        for (Team team : subject.getTeams()) {

            teams.add(new TeamDto(team, false));
        }
        return TeamResponse.builder().teams(teams).build();
    }

    @Transactional(readOnly = true)
    public GetMembersByTeamResponse getMembersByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));

        Long leaderId = team.getLeader().getId();

        List<MembersByTeamDto> members = memberTeamRepository.findByTeamId(teamId)
                .stream().map(memberTeam -> new MembersByTeamDto(memberTeam.getMember(), leaderId)).collect(Collectors.toList());

        return GetMembersByTeamResponse.builder().members(members).build();
    }

    @Transactional(readOnly = true)
    public GetSubscribersNotBelongTeamResponse getSubscribersNotBelongTeam(Long leaderId, Long subjectId) {
        List<Member> subscribers = subscribeRepository.findBySubjectId(subjectId)
                .stream().map(subscribe -> subscribe.getMember()).collect(Collectors.toList());

        List<SubscribersNotBelongTeamDto> subscribersNotBelongTeamDtoList = new ArrayList<>();

        for (Member subscriber : subscribers) {
            boolean notBelongTeam = true;
            for (MemberTeam memberTeam : subscriber.getMemberTeams()) {
                if(memberTeam.getTeam().getSubject().getId().equals(subjectId)){
                    notBelongTeam = false;
                    break;
                }
            }
            if(notBelongTeam) subscribersNotBelongTeamDtoList.add(new SubscribersNotBelongTeamDto(subscriber));
        }

        return GetSubscribersNotBelongTeamResponse.builder().members(subscribersNotBelongTeamDtoList).build();

    }

    public TeamDto deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));

        teamRepository.delete(team);
        return new TeamDto(team, false);
    }

    public LeaveTeamDto leaveTeam(Long memberId, LeaveTeamDto leaveTeamDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Team team = teamRepository.findById(leaveTeamDto.getTeamId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(member.getId(), team.getId())
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));

        if(team.getLeader().getId().equals(member.getId()))
            throw new BusinessException(LEADER_CANT_LEAVE);

        team.leave();
        memberTeamRepository.delete(memberTeam);

        return leaveTeamDto;
    }

    public MandateLeaderDto mandateLeader(Long memberId, MandateLeaderDto mandateLeaderDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Member mandatedLeader = memberRepository.findById(mandateLeaderDto.getMemberId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Team team = teamRepository.findById(mandateLeaderDto.getTeamId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));
        if(!team.getLeader().getId().equals(member.getId()))
            throw new BusinessException(UNAUTHORIZED_REQUEST);
        if(!memberTeamRepository.existsByMemberIdAndTeamId(mandatedLeader.getId(), team.getId()))
            throw new BusinessException(NOT_BELONG_TEAM);

        team.mandateLeader(mandatedLeader);
        teamRepository.save(team);

        return mandateLeaderDto;
    }

    public KickOutMemberDto kickOutMember(Long memberId, KickOutMemberDto kickOutMemberDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        Member kickOutMember = memberRepository.findById(kickOutMemberDto.getMemberId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        Team team = teamRepository.findById(kickOutMemberDto.getTeamId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));

        if(!team.getLeader().getId().equals(member.getId()))
            throw new BusinessException(UNAUTHORIZED_REQUEST);

        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(kickOutMember.getId(), team.getId())
                .orElseThrow(() -> new BusinessException(NOT_BELONG_TEAM));

        if(team.getLeader().getId().equals(kickOutMember.getId()))
            throw new BusinessException(LEADER_CANT_LEAVE);

        team.leave();
        memberTeamRepository.delete(memberTeam);

        return kickOutMemberDto;
    }

    public TeamDto updateTeam(Long memberId, UpdateTeamDto updateTeamDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Team team = teamRepository.findById(updateTeamDto.getTeamId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));
        if(!team.getLeader().getId().equals(member.getId()))
            throw new BusinessException(UNAUTHORIZED_REQUEST);

        team.update(updateTeamDto);
        Team savedTeam = teamRepository.save(team);

        return new TeamDto(savedTeam, false);
    }
}
