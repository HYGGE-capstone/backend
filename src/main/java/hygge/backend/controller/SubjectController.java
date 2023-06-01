package hygge.backend.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.subject.SearchSubjectResponse;
import hygge.backend.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "회원 - 과목", description = "과목 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "과목 검색 메서드", description = "과목 검색 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 등록 성공",
                    content = @Content(schema = @Schema(implementation = SearchSubjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "이력서 등록 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/search")
    public ResponseEntity<SearchSubjectResponse> searchSubject(@RequestParam("query") String query, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        SearchSubjectResponse response = subjectService.searchSubjects(query, memberId);
        return ResponseEntity.ok(response);
    }
}
