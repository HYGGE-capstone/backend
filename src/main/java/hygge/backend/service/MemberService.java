package hygge.backend.service;

import hygge.backend.dto.SignupRequest;
import hygge.backend.dto.SignupResponse;
import hygge.backend.entity.Member;
import hygge.backend.exception.DuplicateException;
import hygge.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) throws RuntimeException {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {  // 이메일 중복
            throw new DuplicateException();
        }
        else if (memberRepository.existsByLoginId(signupRequest.getLoginId())) {  // 로그인 아이디 중복
            throw new DuplicateException();
        }
        Member member = signupRequest.toMember();
        memberRepository.save(member);

        SignupResponse response = new SignupResponse();
        response.setLoginId(member.getLoginId());
        response.setEmail(member.getEmail());

        return response;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findId(String email) {
        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if (foundMember.isPresent()) {
            String loginId = foundMember.get().getLoginId();
            return new ResponseEntity<>(loginId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("아이디 찾기 실패", HttpStatus.NOT_FOUND);
        }
    }
}
