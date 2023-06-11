package hygge.backend.service;

import hygge.backend.dto.jwt.TokenDto;
import hygge.backend.dto.member.MemberDto;
import hygge.backend.dto.member.LogoutRequest;
import hygge.backend.dto.member.ReissueDto;
import hygge.backend.dto.member.LogoutResponse;
import hygge.backend.dto.member.LoginRequest;
import hygge.backend.dto.member.SignupRequest;
import hygge.backend.dto.member.LoginIdResponse;
import hygge.backend.dto.member.LoginResponse;
import hygge.backend.dto.member.SignupResponse;
import hygge.backend.entity.Member;
import hygge.backend.entity.Role;
import hygge.backend.entity.School;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.jwt.TokenProvider;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static hygge.backend.error.exception.ExceptionInfo.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    private final SchoolRepository schoolRepository;

    public SignupResponse signup(SignupRequest signupRequest) throws BusinessException {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {  // 이메일 중복
            throw new BusinessException(REGISTERED_EMAIL);
        }
        else if (memberRepository.existsByLoginId(signupRequest.getLoginId())) {  // 로그인 아이디 중복
            throw new BusinessException(REGISTERED_LOGIN_ID);
        }

        School school = schoolRepository.findById(signupRequest.getSchoolId())
                .orElseThrow(()->new BusinessException(UNREGISTERED_SCHOOL));

        Member member = Member.builder()
                        .loginId(signupRequest.getLoginId())
                        .email(signupRequest.getEmail())
                        .password(passwordEncoder.encode(signupRequest.getPassword()))
                        .nickname(signupRequest.getNickname())
                        .role(Role.ROLE_USER)
                        .school(school)
                        .build();

        memberRepository.save(member);

        SignupResponse response = new SignupResponse();
        response.setLoginId(member.getLoginId());
        response.setEmail(member.getEmail());

        return response;
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

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("MemberService.login()");
        // 1. Login ID/PW 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authentication 메서드가 실행이 될 때
        //    CustomUserDetailService 에서 만들었던 loadUserByUsername 메서드가 실햄됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                                                    tokenDto.getRefreshToken(),
                                                    tokenDto.getRefreshTokenExpiresIn(),
                                                    TimeUnit.MILLISECONDS);

        Member loginMember = memberRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));
        School school = loginMember.getSchool();

        // 5. 토큰 발급
        return LoginResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .loginId(loginMember.getLoginId())
                .nickname(loginMember.getNickname())
                .role(loginMember.getRole().toString())
                .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                .refreshTokenExpiresIn(tokenDto.getRefreshTokenExpiresIn())
                .schoolId(school.getId())
                .schoolName(school.getSchoolName())
                .build();
    }

    public LogoutResponse logout(LogoutRequest request){
        log.info("MemberService.logout()");

        if (!tokenProvider.validateToken(request.getAccessToken())) {
            throw new BusinessException(INVALID_ACCESS_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        Long expiration = tokenProvider.getExpiration(request.getAccessToken());
        redisTemplate.opsForValue().set(request.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return LogoutResponse.builder()
                .memberId(authentication.getName())
                .build();
    }

    public TokenDto reissue(ReissueDto request) {
        log.info("MemberService.reissue()");
        // 1. 리프레쉬 토큰 검증
        if(!tokenProvider.validateToken(request.getRefreshToken()))
            throw new BusinessException(INVALID_REFRESH_TOKEN);

        // 2. 액세스 토큰에서 정보 추출
        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        // 3. Redis 에서 Refresh Token 가져옴
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());

        if(ObjectUtils.isEmpty(refreshToken) || !refreshToken.equals(request.getRefreshToken()))
            throw new BusinessException(INVALID_REFRESH_TOKEN);

        // 4. 새 토큰 발급
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 5. 리프레쉬 토큰 Redis 에 저장
        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpiresIn(),
                TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional(readOnly = true)
    public List<MemberDto> getMembersBySchoolId(Long adminId) {
        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new BusinessException(CANNOT_FIND_MEMBER));

        return memberRepository.findBySchool(admin.getSchool())
                .stream().map(member -> new MemberDto(member)).collect(Collectors.toList());
    }
}
