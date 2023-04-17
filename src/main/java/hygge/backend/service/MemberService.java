package hygge.backend.service;

import hygge.backend.dto.SignupDto;
import hygge.backend.entity.Member;
import hygge.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public boolean signup(SignupDto signupDto) {
        if (memberRepository.existsByEmail(signupDto.getEmail())) {
            return false;
        }
        else if (memberRepository.existsByLoginId(signupDto.getLoginId())) {
            return false;
        }
        Member member = signupDto.toMember();
        memberRepository.save(member);

        return true;
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
