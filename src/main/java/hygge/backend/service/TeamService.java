package hygge.backend.service;

import hygge.backend.dto.TeamDto;
import hygge.backend.dto.notification.NewTeamNotiDto;
import hygge.backend.dto.request.team.CreateTeamRequest;
import hygge.backend.dto.response.team.*;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    private final TeamRepository teamRepository;

    private final MemberTeamRepository memberTeamRepository;

    private final NotificaitonService notificaitonService;
    private final SubscribeRepository subscribeRepository;

    @Transactional
    public CreateTeamResponse createTeam(Long memberId, CreateTeamRequest request) {
        log.info("TeamService.createTeam {}", memberId);

        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        log.info("memberRepository.findById = {}", leader.getId());

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new BusinessException("요청하신 과목이 존재하지 않습니다. "));
        log.info("subjectRepository.findById = {}", subject.getId());

        if (request.getMaxMember() < 2) throw new BusinessException("최대 멤버수가 2보다 작습니다.");

        // 유저가 과목에 속한 팀이 이미 있다면 오류 처리
        Long subjectId;

        for (MemberTeam memberTeam : leader.getMemberTeams()) {
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
                .orElseThrow(() -> new BusinessException("해당하는 회원이 존재하지 않습니다"));

        List<TeamDto> teams = new ArrayList<>();

        for (MemberTeam memberTeam : member.getMemberTeams()) {
            Team team = memberTeam.getTeam();
            Subject subject = team.getSubject();
            boolean isLeader = false;
            if(team.getLeader().getId() == memberId) isLeader = true;

            teams.add(TeamDto.builder()
                            .teamId(team.getId())
                            .teamName(team.getName())
                            .teamTitle(team.getTitle())
                            .teamDescription(team.getDescription())
                            .maxMember(team.getMaxMember())
                            .numMember(team.getNumMember())
                            .isLeader(isLeader)
                            .subjectId(subject.getId())
                            .subjectName(subject.getName())
                            .subjectCode(subject.getCode())
                    .build());
        }
        return TeamResponse.builder().teams(teams).build();
    }

    @Transactional(readOnly = true)
    public TeamResponse searchTeams(Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new BusinessException("해당하는 과목을 찾을 수 없습니다"));

        List<TeamDto> teams = new ArrayList<>();

        for (Team team : subject.getTeams()) {

            teams.add(TeamDto.builder()
                    .teamId(team.getId())
                    .teamName(team.getName())
                    .teamTitle(team.getTitle())
                    .teamDescription(team.getDescription())
                    .maxMember(team.getMaxMember())
                    .numMember(team.getNumMember())
                    .subjectId(subject.getId())
                    .subjectName(subject.getName())
                    .subjectCode(subject.getCode())
                    .build());
        }
        return TeamResponse.builder().teams(teams).build();
    }

    @Transactional(readOnly = true)
    public GetMembersByTeamResponse getMembersByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("요청하신 팀이 존재하지 않습니다."));

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
}
