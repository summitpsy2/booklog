package com.mybooklog.booklog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 1. 이 클래스가 '설정' 파일임을 스프링에게 알려줌
@EnableWebSecurity // 2. 웹 보안을 활성화함
public class SecurityConfig {

    @Bean // 3. 이 메소드가 반환하는 객체를 스프링이 관리하는 'Bean'으로 등록
    public PasswordEncoder passwordEncoder() {
        // 4. BCrypt 알고리즘을 사용하는 비밀번호 암호화 도구를 Bean으로 등록
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf 보호 기능을 비활성화합니다.
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .authorizeHttpRequests(auth -> auth
                        // 5. 아래 경로들은 로그인하지 않아도 누구나 접근 가능하도록 허용
                        .requestMatchers("/", "/css/**", "/js/**", "/signup", "/login", "/h2-console/**").permitAll()
                        // 6. 위에서 허용한 경로 외의 모든 경로는 반드시 '인증'(로그인)을 해야 접근 가능
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // 7. 우리가 직접 만든 로그인 페이지 경로를 지정
                        .loginPage("/login")
                        // 8. 로그인 성공 시 이동할 기본 경로
                        .defaultSuccessUrl("/books", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        // 9. 로그아웃 성공 시 이동할 경로
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
}