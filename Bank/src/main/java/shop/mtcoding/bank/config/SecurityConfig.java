package shop.mtcoding.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import shop.mtcoding.bank.config.jwt.JwtAuthenticationFilter;
/*import shop.mtcoding.bank.config.jwt.JwtAuthorizationFilter;*/
import shop.mtcoding.bank.config.jwt.JwtAuthorizationFilter;
import shop.mtcoding.bank.domain.user.UserEnum;
import shop.mtcoding.bank.util.CustomResponseUtil;

@Configuration
public class SecurityConfig {
    // 로그 보는것
    private final Logger log = LoggerFactory.getLogger(getClass());

    //@Bean 어노테이션이 붙은 메서드는 spring loc 컨테이너 bean으로 등록 @Bean은 sping 애플리케이션에서
    //전반적으로 싱글톤으로 관리
    //@Bean으로 주입 받은 것은 @Auto woird로 주입받아서 쓸수 있음 
    @Bean // Ioc 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨.
    // BCryptPasswordEncoder는 스프링 시큐리티에 제공하는 암호화 클래스
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 등록이 필요함
    // Spring Secuirty 의 HttpSecurity 설정에서 jwt 기반의 인증 JwtAuthenticationFilter 과 JwtAuthorizationFilter
    // 필터를 추가한다 AbstractHttpConfigurer를 상속 받아 커스텀을 설정의 정의한다
    // 첫번쨰 파라미터 : 현재클래스를 지정 CustomSecurityFilterManager 두번쨰 파라미터 설정을 적용할 대상 (HttpSecurity)
    // AbstractHttpConfigurer는 스프링 시큐리티에서 HttpSecurity 설정을 확장하거나 커스터 마이징 할떄 쓸수 있는 추상화 클래스 입니다
    // 이를 상속 받아 커스텀 설정 로직을 구현 할수 있다
    // AuthenticationManager 인증 프로세스를 담당하는 핵심 커포넘트
    // builder.getSharedObject(AuthenticationManager.class)를 통해 HttpSecurity의 공유 객체로부터 AuthenticationManager 가져온다
    // JwtAuthenticationFilter 사용자의 로그인 요청을 처리하고 성공시 jwt토큰을 생성하여 반환하는 필터
    // JwtAuthorizationFilter 각 요청시 jwt 토큰을 검증하고 사용자 기반을 인가하는 필터
    // JwtAuthenticationFilter와 JwtAuthorizationFilter를 HttpSecurity에 추가
    // super.configure(builder)를 호출하여 부모 클래스의 기본 설정
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    // JWT 서버를 만들 예정!! Session 사용안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        http.headers().frameOptions().sameOrigin(); // iframe 허용안함. Clickjacking 공격을 방지하는 데 도움
        http.csrf().disable(); // RESTful API를 사용할 때 일반적으로 CSRF 보호를 비활성
        http.cors().configurationSource(configurationSource()); //클라이언트(예: React, 모바일 앱)에서 API에 접근

        // jSessionId를 서버쪽에서 관리안하겠다는 뜻!!
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 기본 제공되는 폼 기반 로그인을 비활성화 대신  JWT 기반의 커스텀 인증 방식을 사용
        http.formLogin().disable();
        // HTTP Basic 인증을 비활성화
        http.httpBasic().disable();

        // 필터 적용 JwtAuthenticationFilter와 JwtAuthorizationFilter를 HttpSecurity에 추가하는 역할
        http.apply(new CustomSecurityFilterManager());

        // 인증 실패
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
        });

        // 권한 실패 인증은 되었지만, 필요한 권한이 없는 사용자가 접근할 때 호출
        http.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
        });
         //URL 패턴에 따른 접근 제어를 설정
         // /api/s/로 시작하는 모든 요청은 인증된 사용자만 접근
        // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        // /api/admin/으로 시작하는 모든 요청은 ADMIN 역할을 가진 사용자만 접근
        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN) // 최근 공식문서에서는 ROLE_ 안붙여도 됨
                .anyRequest().permitAll(); //위에서 명시된 패턴을 제외한 모든 요청은 인증 없이 접근할수 있음

        return http.build();
    }

   //setAllowedOrigins: 요청을 허용할 도메인을 지정
   //setAllowedMethods: 허용할 HTTP 메서드를 지정
   //setAllowedHeaders: 허용할 HTTP 헤더를 지정
   //setAllowCredentials: 자격 증명(쿠키, 인증 헤더 등)을 포함한 요청을 허용할지 여부를 설정
    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOrigin("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/*
종합적인 보안 설정 흐름
클라이언트 요청:

클라이언트(예: React, 모바일 앱)가 API 엔드포인트에 요청을 보냅니다.
로그인 시도는 /api/login과 같은 엔드포인트로 이루어집니다.
인증 처리:

JwtAuthenticationFilter가 로그인 요청을 처리하고, 성공 시 JWT 토큰을 생성하여 클라이언트에 반환합니다.
인증된 요청:

클라이언트는 이후 요청 시 Authorization 헤더에 Bearer <JWT_TOKEN>을 포함하여 서버에 요청합니다.
토큰 검증:

JwtAuthorizationFilter가 요청 헤더의 토큰을 검증하고, 유효한 경우 SecurityContext에 인증 정보를 설정합니다.
권한 검사:

SecurityFilterChain의 authorizeRequests 설정에 따라 요청된 리소스에 대한 접근 권한을 검사합니다.
권한이 없는 경우 AccessDeniedHandler가 호출되어 403 FORBIDDEN 응답을 반환합니다.
응답 처리:

요청이 성공적으로 처리되면, 클라이언트는 필요한 데이터를 수신합니다.
인증 또는 권한 오류가 발생하면, 클라이언트는 적절한 오류 메시지와 상태 코드를 받습니다.
 */