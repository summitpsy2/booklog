package com.mybooklog.booklog.config;

import com.mybooklog.booklog.service.UserDetailsServiceImpl; // import
import lombok.RequiredArgsConstructor; // import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider; // import
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // import
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // final 필드 주입을 위해 추가
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // 우리가 만든 서비스로 사용자 조회
        provider.setPasswordEncoder(passwordEncoder()); // 우리가 만든 암호화 도구 사용
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/api/**") // API와 H2 콘솔은 CSRF 예외
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // H2 콘솔 프레임 허용
                )
                .authorizeHttpRequests(auth -> auth
                        // CSS, JS, H2, 홈, 회원가입, 로그인은 누구나 접근 가능
                        .requestMatchers("/", "/css/**", "/js/**", "/signup", "/login", "/h2-console/**").permitAll()
                        // API 경로는 인증된 사용자만
                        .requestMatchers("/api/**").authenticated()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form // 웹 브라우저용 폼 로그인 설정
                        .loginPage("/login")
                        .defaultSuccessUrl("/books", true)
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // API 클라이언트용 Basic 인증 설정
                .logout(logout -> logout // 로그아웃 설정
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
}