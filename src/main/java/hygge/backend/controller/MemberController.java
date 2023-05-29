package hygge.backend.controller;

import hygge.backend.dto.error.ErrorResponse;
import hygge.backend.dto.jwt.TokenDto;
import hygge.backend.dto.member.*;
import hygge.backend.dto.member.request.LogoutRequest;
import hygge.backend.dto.member.response.LogoutResponse;
import hygge.backend.service.EmailService;
import hygge.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 관련 API 입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @Operation(summary = "회원가입 메서드", description = "회원가입 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {

        SignupResponse response = memberService.signup(signupRequest);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인 아이디 중복 검사 메서드", description = "입력한 로그인 아이디가 이미 존재하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입력한 로그인 아이디는 사용가능 합니다.",
                    content = @Content(schema = @Schema(implementation = LoginIdResponse.class)))
    })
    @GetMapping("/signup/loginId/{loginId}")
    public ResponseEntity<LoginIdResponse> checkLoginId(@PathVariable String loginId) {
        return memberService.checkLoginId(loginId);
    }

    // 이메일 인증 메서드
    @Operation(summary = "이메일 인증 메서드", description = "이메일 인증을 통해 서버에 등록된 학교의 학생임을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 코드 전송 성공.",
                    content = @Content(schema = @Schema(implementation = EmailAuthResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "이메일 인증 코드 전송 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/signup/email/auth")
    public ResponseEntity<EmailAuthResponse> sendEmail(@RequestParam String to) throws Exception {
        EmailAuthResponse emailAuthResponse = emailService.sendEmail(to);
        return ResponseEntity.ok(emailAuthResponse);
    }

    @Operation(summary = "로그인 메서드", description = "로그인 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그인 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }

    @Operation(summary = "로그아웃 메서드", description = "로그아웃 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = LogoutResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
        return ResponseEntity.ok(memberService.logout(request));
    }

    @Operation(summary = "토큰 재발급 메서드", description = "토큰 재발급 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = TokenDto.class))),
            @ApiResponse(responseCode = "400", description = "토큰 재발급 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody ReissueDto request) {
        return ResponseEntity.ok(memberService.reissue(request));
    }

    // 아이디 찾기 메서드
//    @GetMapping("/findid/{email}")
//    public ResponseEntity<?> findId(@PathVariable String email) {
//        return memberService.findId(email);
//    }
}

