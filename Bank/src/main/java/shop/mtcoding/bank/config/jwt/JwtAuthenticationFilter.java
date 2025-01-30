package shop.mtcoding.bank.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.user.UserReqDto.LoginReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.LoginRespDto;
import shop.mtcoding.bank.util.CustomResponseUtil;
//인증(Authentication) 사용자의 자격 증명(예: 사용자명과 비밀번호)을 검증하고, 검증이 성공하면 JWT 토큰을 생성하여 클라이언트에 반환 주로 로그인 요청 시에 동작
//JwtAuthenticationFilter는 Spring Security에서 JWT(JSON Web Token)를 사용한 인증 과정을 처리하기 위해 UsernamePasswordAuthenticationFilter를 확장한 커스텀 필터
//이 필터는 사용자가 로그인할 때 제공한 자격 증명을 검증하고, 인증이 성공하면 JWT 토큰을 생성하여 클라이언트에 반환
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    //생성자
    //AuthenticationManager 주입 받아 초기화
    //super(authenticationManager): 부모 클래스인 UsernamePasswordAuthenticationFilter에 AuthenticationManager를 전달
    //UsernamePasswordAuthenticationFilter의 기본 URL(/login)을 커스터마이징하여 /api/login으로 변경
    //AuthenticationManager를 주입받아 필터 내부에서 인증을 처리할 수 있도록 설정
    //authenticationManager: 인증 요청을 처리하는 매니저로, 사용자 인증을 실제로 수행
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }
   //JwtAuthenticationFilter는 attemptAuthentication과 successfulAuthentication 메서드를 오버라이드하여 사용자 인증과 JWT 생성 로직을 구현
    // Post : /api/login
    //클라이언트로부터 받은 로그인 요청을 처리하여 인증을 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 호출됨");
        try {
            ObjectMapper om = new ObjectMapper(); //ObjectMapper를 사용하여 HTTP 요청의 입력 스트림에서 LoginReqDto 객체로 변환
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class); //LoginReqDto는 사용자 로그인 정보를 담는 DTO

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getUsername(), loginReqDto.getPassword());

           //authenticationManager.authenticate(authenticationToken)을 호출하여 인증을 시도
            //인증이 성공하면 Authentication 객체를 반환
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        } catch (Exception e) {
            // unsuccessfulAuthentication 호출함
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인실패", HttpStatus.UNAUTHORIZED);
    }

    // return authentication 잘 작동하면 successfulAuthentication 메서드 호출됩니다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication 호출됨");
        LoginUser loginUser = (LoginUser) authResult.getPrincipal(); //authResult.getPrincipal()을 통해 인증된 사용자 정보를 가져옴
        String jwtToken = JwtProcess.create(loginUser); //메서드를 사용하여 JWT 토큰을 생성
        response.addHeader(JwtVO.HEADER, jwtToken); //생성된 JWT 토큰을 Authorization 헤더에 추가하여 클라이언트에 전달

        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser()); //LoginRespDto 객체를 생성하여 사용자 정보를 담음
        CustomResponseUtil.success(response, loginRespDto); //CustomResponseUtil.success 메서드를 사용하여 200 OK 상태 코드와 함께 JSON 형식으로 응답
    }

}