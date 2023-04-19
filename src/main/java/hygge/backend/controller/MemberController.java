package hygge.backend.controller;

import hygge.backend.dto.SignupRequest;
import hygge.backend.dto.SignupResponse;
import hygge.backend.exception.DuplicateException;
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
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

    @GetMapping("/signup/email/{email}")
    public ResponseEntity checkEmail(@PathVariable String email){
        return memberService.checkEmail(email);
    }

    @GetMapping("/signup/loginid/{loginId}")
    public ResponseEntity checkLoginId(@PathVariable String loginId){
        return memberService.checkLoginId(loginId);
    }



    @GetMapping("/findid/{email}")
    public ResponseEntity<?> findId(@PathVariable String email) {
        return memberService.findId(email);
    }
}

