package hygge.backend.service;

import hygge.backend.dto.notice.NoticeDto;
import hygge.backend.dto.notice.PostNoticeDto;
import hygge.backend.dto.notice.UpdateNoticeDto;
import hygge.backend.entity.Member;
import hygge.backend.entity.Notice;
import hygge.backend.entity.Team;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.error.exception.ExceptionInfo;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.NoticeRepository;
import hygge.backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoticeService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final NoticeRepository noticeRepository;

    public NoticeDto postNotice(Long memberId, PostNoticeDto postNoticeDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));
        Team team = teamRepository.findById(postNoticeDto.getTeamId())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_TEAM));
        if(!team.getLeader().getId().equals(member.getId()))
            throw new BusinessException(ExceptionInfo.UNAUTHORIZED_REQUEST);
        if (team.getNotice() != null)
            throw new BusinessException(ExceptionInfo.ALREADY_HAVE_NOTICE);

        Notice savedNotice = noticeRepository.save(postNoticeDto.toEntity());
        team.postNotice(savedNotice);
        teamRepository.save(team);

        return new NoticeDto(savedNotice, team);
    }

    public NoticeDto updateNotice(Long memberId, UpdateNoticeDto updateNoticeDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));
        Notice notice = noticeRepository.findById(updateNoticeDto.getNoticeId())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_NOTICE));
        if(!notice.getTeam().getLeader().getId().equals(member.getId()))
            throw new BusinessException(ExceptionInfo.UNAUTHORIZED_REQUEST);

        notice.updateContent(updateNoticeDto.getNoticeContent());
        Notice savedNotice = noticeRepository.save(notice);

        return new NoticeDto(savedNotice);
    }
}
