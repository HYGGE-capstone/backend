package hygge.backend.controller;

import hygge.backend.dto.SignupDto;
import hygge.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto) {
        if (memberService.signup(signupDto)) {
            return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("회원가입 실패",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findid/{email}")
    public ResponseEntity<?> findId(@PathVariable String email) {
        return memberService.findId(email);
    }
}

