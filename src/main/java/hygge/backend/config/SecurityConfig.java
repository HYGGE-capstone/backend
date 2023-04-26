package hygge.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 사용 안함
                .and()
                .addFilter(corsFilter)  // Security Filter에 등록 / @CrossOrigin (인증 x)
                .formLogin().disable()  // Form login 안함
                .httpBasic().disable()
                .authorizeRequests()
                // 요청 허용 설정

                // 그 외 모든 요청 허용
                .anyRequest().permitAll();

        return http.build();
    }
}
