package hygge.backend.controller;

import hygge.backend.dto.SubjectDto;
import hygge.backend.dto.SubscribeDto;
import hygge.backend.entity.Subscribe;
import hygge.backend.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    // 구독 메서드
    @PostMapping("/subscribe")
    public ResponseEntity<SubscribeDto> subscribe(Principal principal, @RequestBody SubjectDto subjectDto) {
        Long memberId = Long.parseLong(principal.getName());
        Long subjectId = subjectDto.getSubjectId();
        return ResponseEntity.ok(subscribeService.subscribe(new SubscribeDto(memberId, subjectId)));
    }

    // 구독 해제 메서드
    @PostMapping("/unsubscribe")
    public ResponseEntity<SubscribeDto> unsubscribe(Principal principal, @RequestBody SubjectDto subjectDto) {
        Long memberId = Long.parseLong(principal.getName());
        Long subjectId = subjectDto.getSubjectId();
        return ResponseEntity.ok(subscribeService.unsubscribe(new SubscribeDto(memberId, subjectId)));
    }
}
