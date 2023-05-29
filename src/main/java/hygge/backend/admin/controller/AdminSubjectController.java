package hygge.backend.admin.controller;

import hygge.backend.dto.subject.SubjectDto;
import hygge.backend.dto.subject.SubjectNoIdDto;
import hygge.backend.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "0 관리자 - 과목", description = "과목(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/subject")
@RequiredArgsConstructor
public class AdminSubjectController {

    private final SubjectService subjectService;

    // 학교에 등록된 과목 검색
    @GetMapping
    public List<SubjectDto> getSubjectsBySchoolId(@RequestParam Long schoolId) {
        return subjectService.getSubjectsBySchoolId(schoolId);
    }

     // 새로운 과목 등록
    @PostMapping
    public SubjectDto enrollSubject(@RequestBody SubjectNoIdDto request) {
        return subjectService.enrollSubject(request);
    }

    // 과목 수정
    @PutMapping
    public SubjectDto fixSubject(@RequestBody SubjectDto request) {
        return subjectService.fixSubject(request);
    }

    // 과목 삭제(팀 있으면 삭제 x)
    @DeleteMapping
    public SubjectDto deleteSubject(@RequestParam Long subjectId) {
        return subjectService.deleteSubject(subjectId);
    }
}
