package hygge.backend.admin.controller;

import hygge.backend.dto.member.MemberDto;
import hygge.backend.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "0 관리자 - 회원", description = "회원(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    // 학교에 등록된 멤버 조회
    @GetMapping
    public List<MemberDto> getMembersBySchoolId(@RequestParam Long schoolId) {
        return memberService.getMembersBySchoolId(schoolId);
    }
}
