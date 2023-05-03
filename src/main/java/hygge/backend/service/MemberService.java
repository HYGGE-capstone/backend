package hygge.backend.service;

import hygge.backend.dto.TokenDto;
import hygge.backend.dto.request.LoginRequest;
import hygge.backend.dto.request.SignupRequest;
import hygge.backend.dto.request.TokenRequest;
import hygge.backend.dto.response.EmailResponse;
import hygge.backend.dto.response.LoginIdResponse;
import hygge.backend.dto.response.SignupResponse;
import hygge.backend.entity.Member;
import hygge.backend.entity.RefreshToken;
import hygge.backend.error.exception.DuplicateException;
import hygge.backend.jwt.TokenProvider;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) throws RuntimeException {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {  // 이메일 중복
            throw new DuplicateException();
        }
        else if (memberRepository.existsByLoginId(signupRequest.getLoginId())) {  // 로그인 아이디 중복
            throw new DuplicateException();
        }

        Member member = signupRequest.toMember(passwordEncoder);
        memberRepository.save(member);

        SignupResponse response = new SignupResponse();
        response.setLoginId(member.getLoginId());
        response.setEmail(member.getEmail());

        return response;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<EmailResponse> checkEmail(String email) {
        EmailResponse response = new EmailResponse();
        response.setEmail(email);
        if(memberRepository.existsByEmail(email)){
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<LoginIdResponse> checkLoginId(String loginId) {
        LoginIdResponse response = new LoginIdResponse();
        response.setLoginId(loginId);
        if (memberRepository.existsByLoginId(loginId)) {
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(response, HttpStatus.OK);
        }
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

    @Transactional
    public TokenDto login(LoginRequest loginRequest) {
        // 1. Login ID/PW 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authentication 메서드가 실행이 될 때
        //    CustomUserDetailService 에서 만들었던 loadUserByUsername 메서드가 실햄됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequest tokenRequest) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken());

        // 3. 저장소에서 Member ID를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}
