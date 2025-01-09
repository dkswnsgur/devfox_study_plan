package shop.mtcoding.bank.config;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.mtcoding.bank.domain.user.UserEnum;

@Slf4j
@Configuration
@EnableWebSecurity //Spring Security의 웹 보안을 활성화하는 어노테이션. 이 어노테이션을 사용하면 Spring Security의 기본 보안 구성이 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { //BCryptPasswordEncoder는 비밀번호를 암호화할 때 사용하는 클래스
        log.debug("디버그: BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { //CORS(Cross-Origin Resource Sharing) 설정을 담당한다. 이 설정은 다른 도메인에서 오는 요청에 대해 허용할 헤더, 메소드, 출처 등을 설정할 수 있게 해줌
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); //모든 헤더를 허용.
        configuration.addAllowedMethod("*"); //모든 HTTP 메소드를 허용.
        configuration.addAllowedOrigin("*"); //모든 출처를 허용.
        configuration.setAllowCredentials(true); //자격 증명(Cookie, HTTP 인증 등)을 허용.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //URL에 대한 CORS 설정을 등록하는 데 사용됩. 여기서는 모든 URL에 대해 CORS 설정을 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("디버그: HttpSecurity 설정");

        http.headers().frameOptions().disable(); //헤더를 비활성화하여, 애플리케이션이 다른 웹 페이지에서 iframe으로 로드될 수 있도록 허용함. 주로 H2 데이터베이스 콘솔을 사용할 때 필요
        http.csrf().disable(); //CSRF(Cross-Site Request Forgery) 공격을 방지하는 기능을 비활성. REST API 서버에서 주로 사용
        http.cors().configurationSource(corsConfigurationSource()); //CORS 설정을 활성. corsConfigurationSource 메소드를 통해 설정된 CORS 정책을 적용

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //세션 관리를 STATELESS로 설정합니다. 이 설정은 서버에서 세션을 생성하거나 유지하지 않도록 함. 일반적으로 JWT 기반 인증 시스템에서 사용
        http.formLogin().disable(); // 기본 로그인 폼을 비활성화. 클라이언트에서 커스텀 로그인 방식이나 다른 인증 방법을 사용하도록 설정할 때 필요
        http.httpBasic().disable(); //HTTP 기본 인증을 비활성화합

        http.authorizeRequests() //URL 경로에 대한 접근 제어를 설정
                .antMatchers("/api/s/**").authenticated() ///api/s/로 시작하는 모든 요청은 인증된 사용자만 접근 가능.
                .antMatchers("/api/admin/**").hasRole("ADMIN") // /api/admin/로 시작하는 요청은 ADMIN 역할을 가진 사용자만 접근 가능
                .anyRequest().permitAll(); //나머지 모든 요청은 인증 없이 허용
    }
}
