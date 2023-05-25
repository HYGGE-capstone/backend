package hygge.backend.service;

import hygge.backend.dto.message.MessageDto;
import hygge.backend.dto.message.SendMessageRequest;
import hygge.backend.entity.Member;
import hygge.backend.entity.Message;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.error.exception.ExceptionInfo;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MessageDto sendMessage(Long fromMemberId, SendMessageRequest request) {
        Member from = memberRepository.findById(fromMemberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        Member to = memberRepository.findById(request.getTo())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        Message message = Message.builder()
                .from(from)
                .to(to)
                .content(request.getContent())
                .isOpened(false)
                .build();

        Message sentMessage = messageRepository.save(message);
        return new MessageDto(sentMessage);
    }
}
