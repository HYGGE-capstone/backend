package hygge.backend.service;

import hygge.backend.dto.SignupDto;
import hygge.backend.entity.Member;
import hygge.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
}
