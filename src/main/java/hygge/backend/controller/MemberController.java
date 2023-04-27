package hygge.backend.controller;

import hygge.backend.dto.request.SignupRequest;
import hygge.backend.dto.response.EmailResponse;
import hygge.backend.dto.response.LoginIdResponse;
import hygge.backend.dto.response.SignupResponse;
import hygge.backend.exception.DuplicateException;
import hygge.backend.jwt.JwtService;
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

@Tag(name = "회원", description = "회원 관련 api 입니다.")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Operation(summary = "회원가입 메서드", description = "회원가입 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "400", description = "중복된 이메일 또는 아이디"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {

        SignupResponse response;
        try{
            response = memberService.signup(signupRequest);
        }catch(DuplicateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "이메일 중복 검사 메서드", description = "입력한 이메일이 이미 존재하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입력한 이메일은 사용가능 합니다.",
                    content = @Content(schema = @Schema(implementation = EmailResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력한 이메일은 이미 존재합니다.")
    })
    @GetMapping("/signup/email/{email}")
    public ResponseEntity<EmailResponse> checkEmail(@PathVariable String email){
        return memberService.checkEmail(email);
    }

    @Operation(summary = "로그인 아이디 중복 검사 메서드", description = "입력한 로그인 아이디가 이미 존재하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입력한 로그인 아이디는 사용가능 합니다.",
                    content = @Content(schema = @Schema(implementation = LoginIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력한 로그인 아이디는 이미 존재합니다.")
    })
    @GetMapping("/signup/loginId/{loginId}")
    public ResponseEntity<LoginIdResponse> checkLoginId(@PathVariable String loginId){
        return memberService.checkLoginId(loginId);
    }

    @PostMapping("/signup/email/auth")
    public String sendEmail(@RequestParam String to) throws Exception {
        return emailService.sendEmail(to);
    }

    @GetMapping("/findid/{email}")
    public ResponseEntity<?> findId(@PathVariable String email) {
        return memberService.findId(email);
    }

}

