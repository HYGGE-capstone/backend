package hygge.backend.service;

import hygge.backend.dto.message.MessageDto;
import hygge.backend.dto.message.MessageRoomDto;
import hygge.backend.dto.message.SendMessageRequest;
import hygge.backend.entity.Member;
import hygge.backend.entity.Message;
import hygge.backend.entity.MessageRoom;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.error.exception.ExceptionInfo;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.MessageRepository;
import hygge.backend.repository.MessageRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    private final MessageRoomRepository messageRoomRepository;

    @Transactional
    public MessageDto sendMessage(Long fromMemberId, SendMessageRequest request) {
        Member from = memberRepository.findById(fromMemberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        Member to = memberRepository.findById(request.getTo())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        MessageRoom fromMessageRoom = messageRoomRepository.findByFromAndTo(from, to)
                .orElseGet(() -> new MessageRoom(from, to));

        MessageRoom toMessageRoom = messageRoomRepository.findByFromAndTo(to, from)
                .orElseGet(() -> new MessageRoom(to, from));

        MessageRoom savedFromMessageRoom = messageRoomRepository.save(fromMessageRoom);
        MessageRoom savedToMessageRoom = messageRoomRepository.save(toMessageRoom);


        Message fromMessage = Message.builder()
                .from(from)
                .to(to)
                .content(request.getContent())
                .messageRoom(savedFromMessageRoom)
                .isOpened(false)
                .build();

        Message toMessage = Message.builder()
                .from(to)
                .to(from)
                .content(request.getContent())
                .messageRoom(savedToMessageRoom)
                .isOpened(false)
                .build();

        messageRepository.save(fromMessage);
        messageRepository.save(toMessage);

        return new MessageDto(fromMessage);
    }

    @Transactional(readOnly = true)
    public List<MessageRoomDto> getMessageRoom(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        List<MessageRoomDto> messageRoomDtos = messageRoomRepository.findByFrom(member)
                .stream().map(messageRoom -> new MessageRoomDto(messageRoom)).collect(Collectors.toList());

        return messageRoomDtos;
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(Long memberId, Long roomId) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MESSAGE_ROOM));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_MEMBER));

        if(!messageRoom.getFrom().getId().equals(member.getId()))
            throw new BusinessException(ExceptionInfo.UNAUTHORIZED_REQUEST);

        return messageRoom.getMessages()
                .stream().map(message -> new MessageDto(message)).collect(Collectors.toList());
    }
}
