package hygge.backend.controller;

import hygge.backend.dto.TokenDto;
import hygge.backend.dto.request.LoginRequest;
import hygge.backend.dto.request.SignupRequest;
import hygge.backend.dto.response.*;
import hygge.backend.error.exception.DuplicateException;
import hygge.backend.service.EmailService;
import hygge.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @Operation(summary = "회원가입 메서드", description = "회원가입 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignupResponse.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {

        SignupResponse response;
        try {
            response = memberService.signup(signupRequest);
        } catch (DuplicateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "이메일 중복 검사 메서드", description = "입력한 이메일이 이미 존재하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입력한 이메일은 사용가능 합니다.",
                    content = @Content(schema = @Schema(implementation = EmailResponse.class)))
    })
    @GetMapping("/signup/email/{email}")
    public ResponseEntity<EmailResponse> checkEmail(@PathVariable String email) {
        return memberService.checkEmail(email);
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
            @ApiResponse(responseCode = "400", description = "이메일 형식이 서버에 등록되지 않은 형식이다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/signup/email/auth")
    public ResponseEntity<EmailAuthResponse> sendEmail(@RequestParam String to) throws Exception {
        EmailAuthResponse emailAuthResponse = emailService.sendEmail(to);
        return ResponseEntity.ok(emailAuthResponse);
    }

    // 아이디 찾기 메서드
    @GetMapping("/findid/{email}")
    public ResponseEntity<?> findId(@PathVariable String email) {
        return memberService.findId(email);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }
}

