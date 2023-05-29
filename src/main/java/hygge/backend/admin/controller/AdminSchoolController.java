package hygge.backend.admin.controller;

import hygge.backend.dto.school.SchoolDto;
import hygge.backend.dto.school.SchoolNoIdDto;
import hygge.backend.service.SchoolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "0 관리자 - 학교", description = "학교(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/school")
@RequiredArgsConstructor
public class AdminSchoolController {

    private final SchoolService schoolService;

    // 등록된 학교 정보 가져오기
    @GetMapping
    public List<SchoolDto> getSchools() {
        return schoolService.getSchools();
    }

    // 학교 추가
    @PostMapping
    public SchoolDto postSchool(@RequestBody SchoolNoIdDto request) {
        return schoolService.postSchool(request);
    }

    // 학교 정보 수정
    @PutMapping
    public SchoolDto fixSchool(@RequestBody SchoolDto request) {
        return schoolService.fixSchool(request);
    }
}
