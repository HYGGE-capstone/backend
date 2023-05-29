package hygge.backend.service;

import hygge.backend.dto.offer.OfferResultDto;
import hygge.backend.dto.offer.OfferTeamDto;
import hygge.backend.dto.notification.NewOfferResultNotiDto;
import hygge.backend.dto.notification.NewTeamMemberNotiDto;
import hygge.backend.dto.request.offer.OfferRequest;
import hygge.backend.dto.request.offer.OfferResultRequestDto;
import hygge.backend.dto.response.offer.GetOffersResponse;
import hygge.backend.dto.response.offer.OfferResponse;
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
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final TeamRepository teamRepository;

    private final MemberTeamRepository memberTeamRepository;
    private final NotificaitonService notificaitonService;

    @Transactional(readOnly = true)
    public GetOffersResponse getOffers(Long memberId, Long subjectId) {
        log.info("OfferService.getOffers()");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException(CANNOT_FIND_MEMBER));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()-> new BusinessException(CANNOT_FIND_SUBJECT));

        List<Offer> offers = offerRepository.findByMemberId(memberId);

        Long offerSubjectId;

        List<OfferTeamDto> offerTeams = new ArrayList<>();

        for (Offer offer : offers) {
            offerSubjectId = offer.getTeam().getSubject().getId();
            if (offerSubjectId.equals(subjectId)) {
                offerTeams.add(new OfferTeamDto(offer.getTeam(), offer.getId()));
            }
        }

        return GetOffersResponse.builder().offerTeams(offerTeams).build();
    }

    @Transactional
    public OfferResponse offer(Long memberId, OfferRequest request) {
        log.info("OfferService.offer()");
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException(CANNOT_FIND_MEMBER));

        Member subscriber = memberRepository.findById(request.getSubscriberId())
                .orElseThrow(()-> new BusinessException(CANNOT_FIND_MEMBER));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(()-> new BusinessException(CANNOT_FIND_TEAM));

        if(!team.getLeader().getId().equals(leader.getId())) throw new BusinessException(UNAUTHORIZED_REQUEST);

        if (offerRepository.existsByTeamIdAndMemberId(team.getId(), subscriber.getId())) {
            throw new BusinessException(DUPLICATED_OFFER);
        }

        // 해당 과목에 이미 소속된 팀이 있는지 검증
        List<Team> belongTeams = subscriber.getMemberTeams()
                .stream().map(memberTeam -> memberTeam.getTeam()).collect(Collectors.toList());

        for (Team belongTeam : belongTeams) {
            if(belongTeam.getSubject().getId().equals(team.getSubject().getId()))
                throw new BusinessException(ALREADY_HAVE_TEAM);
        }

        Offer offer = Offer.builder()
                .member(subscriber)
                .team(team)
                .build();

        Offer savedOffer = offerRepository.save(offer);

        return new OfferResponse(savedOffer);
    }

    @Transactional
    public OfferResultDto offerAccept(Long memberId, OfferResultRequestDto request) {
        log.info("OfferService.offerAccept()");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_OFFER));
        Team team = offer.getTeam();

        List<Offer> offers = offerRepository.findByMemberId(memberId);

        if(team.getNumMember() + 1 > team.getMaxMember()) throw new BusinessException(MAX_TEAM_MEMBER);

        MemberTeam memberTeam = MemberTeam.builder().member(member).team(team).build();
        memberTeamRepository.save(memberTeam);

        team.joinMember();
        teamRepository.save(team);

        for (Offer offer1 : offers) {
            if (offer1.getTeam().getSubject().getId().equals(team.getSubject().getId())) {
                offerRepository.delete(offer1);
            }
        }

        // 새로운 팀원 알림
        NewTeamMemberNotiDto newTeamMemberNotiDto = NewTeamMemberNotiDto.builder()
                .memberLoginId(member.getLoginId())
                .memberNickname(member.getNickname())
                .teamName(team.getName())
                .teamId(team.getId())
                .build();

        notificaitonService.sendNotification(NotificationCase.NEW_TEAM_MEMBER, newTeamMemberNotiDto);

        // 제안 수락 알림
        NewOfferResultNotiDto newOfferResultNotiDto = NewOfferResultNotiDto.builder()
                .accept(true)
                .applicantLoginId(member.getLoginId())
                .applicantNickname(member.getNickname())
                .leaderId(team.getLeader().getId())
                .teamName(team.getName())
                .build();

        notificaitonService.sendNotification(NotificationCase.NEW_OFFER_RESULT, newOfferResultNotiDto);


        return OfferResultDto.builder()
                .subscriberId(member.getId())
                .teamId(team.getId())
                .result("ACCEPT")
                .build();
    }

    @Transactional
    public OfferResultDto offerReject(Long memberId, OfferResultRequestDto request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_OFFER));

        Long teamId = offer.getTeam().getId();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_TEAM));


        offerRepository.delete(offer);

        // 제안 거절 알림
        NewOfferResultNotiDto newOfferResultNotiDto = NewOfferResultNotiDto.builder()
                .accept(false)
                .applicantLoginId(member.getLoginId())
                .applicantNickname(member.getNickname())
                .leaderId(team.getLeader().getId())
                .teamName(team.getName())
                .build();

        notificaitonService.sendNotification(NotificationCase.NEW_OFFER_RESULT, newOfferResultNotiDto);

        return OfferResultDto.builder()
                .subscriberId(memberId)
                .teamId(teamId)
                .result("REJECT")
                .build();

    }
}
