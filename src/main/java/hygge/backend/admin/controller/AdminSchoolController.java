package hygge.backend.admin.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.member.MemberDto;
import hygge.backend.dto.school.SchoolDto;
import hygge.backend.dto.school.SchoolNoIdDto;
import hygge.backend.service.SchoolService;
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

@Tag(name = "관리자 - 학교", description = "학교(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/school")
@RequiredArgsConstructor
public class AdminSchoolController {

    private final SchoolService schoolService;

    @Operation(summary = "등록된 학교 조회 메서드", description = "등록된 학교 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SchoolDto.class)))),
            @ApiResponse(responseCode = "400", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public List<SchoolDto> getSchools() {
        return schoolService.getSchools();
    }

    @Operation(summary = "학교 추가 메서드", description = "학교 추가 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학교 추가 성공",
                    content = @Content(schema = @Schema(implementation = SchoolDto.class))),
            @ApiResponse(responseCode = "400", description = "학교 추가 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    public SchoolDto postSchool(@RequestBody SchoolNoIdDto request) {
        return schoolService.postSchool(request);
    }

    @Operation(summary = "학교 정보 수정 메서드", description = "학교 정보 수정 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = SchoolDto.class))),
            @ApiResponse(responseCode = "400", description = "정보 수정 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PutMapping
    public SchoolDto fixSchool(@RequestBody SchoolDto request) {
        return schoolService.fixSchool(request);
    }
}
