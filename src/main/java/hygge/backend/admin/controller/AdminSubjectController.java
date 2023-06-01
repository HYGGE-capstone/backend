package hygge.backend.admin.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.member.MemberDto;
import hygge.backend.dto.subject.SubjectDto;
import hygge.backend.dto.subject.SubjectNoIdDto;
import hygge.backend.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 - 과목", description = "과목(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/subject")
@RequiredArgsConstructor
public class AdminSubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "학교에 등록된 과목 조회 메서드", description = "학교에 등록된 과목 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubjectDto.class)))),
            @ApiResponse(responseCode = "400", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<SubjectDto> getSubjectsBySchoolId(@RequestParam Long schoolId) {
        return subjectService.getSubjectsBySchoolId(schoolId);
    }

     @Operation(summary = "과목 추가 메서드", description = "과목 추가 메서드입니다.")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "추가 성공",
                     content = @Content(schema = @Schema(implementation = SubjectDto.class))),
             @ApiResponse(responseCode = "400", description = "추가 실패",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
     })
    @PostMapping
    public SubjectDto enrollSubject(@RequestBody SubjectNoIdDto request) {
        return subjectService.enrollSubject(request);
    }

    @Operation(summary = "과목 수정 메서드", description = "과목 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "수정 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public SubjectDto fixSubject(@RequestBody SubjectDto request) {
        return subjectService.fixSubject(request);
    }

    @Operation(summary = "과목 삭제 메서드", description = "과목 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public SubjectDto deleteSubject(@RequestParam Long subjectId) {
        return subjectService.deleteSubject(subjectId);
    }
}
