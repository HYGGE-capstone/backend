package hygge.backend.controller;

import hygge.backend.dto.ResumeDto;
import hygge.backend.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeDto> postResume(Principal principal, @RequestBody ResumeDto request) {
        Long memberId = Long.parseLong(principal.getName());

        ResumeDto response = resumeService.postResume(memberId, request);
        return ResponseEntity.ok(response);
    }
}
