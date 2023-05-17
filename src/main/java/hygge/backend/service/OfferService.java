package hygge.backend.service;

import hygge.backend.dto.ApplicantDto;
import hygge.backend.dto.OfferTeamDto;
import hygge.backend.dto.response.offer.GetOffersResponse;
import hygge.backend.dto.response.teamapplicant.GetApplicantsResponse;
import hygge.backend.entity.Member;
import hygge.backend.entity.Offer;
import hygge.backend.entity.Subject;
import hygge.backend.entity.Team;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.OfferRepository;
import hygge.backend.repository.SubjectRepository;
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
public class OfferService {
    private final OfferRepository offerRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    @Transactional(readOnly = true)
    public GetOffersResponse getOffers(Long memberId, Long subjectId) {
        log.info("OfferService.getOffers");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException("요청하신 멤버가 존재하지 않습니다."));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()-> new BusinessException("요청하신 과목이 존재하지 않습니다."));

        List<Offer> offers = offerRepository.findByMemberId(memberId);

        Long offerSubjectId;

        List<OfferTeamDto> offerTeams = new ArrayList<>();

        for (Offer offer : offers) {
            offerSubjectId = offer.getTeam().getSubject().getId();
            if (offerSubjectId == subjectId) {
                offerTeams.add(new OfferTeamDto(offer.getTeam()));
            }
        }

        return GetOffersResponse.builder().offerTeams(offerTeams).build();
    }
}
