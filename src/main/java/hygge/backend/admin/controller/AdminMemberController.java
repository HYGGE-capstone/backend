package hygge.backend.admin.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.member.ChangeNickNameDto;
import hygge.backend.dto.member.MemberDto;
import hygge.backend.dto.member.SignupResponse;
import hygge.backend.dto.message.MessageRoomDto;
import hygge.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "관리자 - 회원", description = "회원(관리자) 관련 API 입니다.")
@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @Operation(summary = "학교에 등록된 멤버 조회 메서드", description = "학교에 등록된 멤버 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberDto.class)))),
            @ApiResponse(responseCode = "400", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public List<MemberDto> getMembersBySchoolId(Principal principal) {
        Long adminId = Long.parseLong(principal.getName());
        return memberService.getMembersBySchoolId(adminId);
    }

    @PostMapping("/nickname")
    public MemberDto changeMemberNickname(@RequestBody ChangeNickNameDto changeNickNameDto) {
        return memberService.changeNickname(changeNickNameDto);
    }
}
