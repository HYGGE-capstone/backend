package hygge.backend.controller;

import hygge.backend.dto.response.subject.SearchSubjectResponse;
import hygge.backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/search")
    public ResponseEntity<SearchSubjectResponse> searchSubject(@RequestParam("query") String query) {
        SearchSubjectResponse response = subjectService.searchSubjects(query);
        return ResponseEntity.ok(response);
    }
}
