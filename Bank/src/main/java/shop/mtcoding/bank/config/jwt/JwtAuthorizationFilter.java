package shop.mtcoding.bank.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import shop.mtcoding.bank.config.auth.LoginUser;

/*
 * 모든 주소에서 동작함 (토큰 검증)
 * 인가(Authorization) 목적: 클라이언트로부터 전달받은 JWT 토큰을 검증하고, 토큰이 유효하면 사용자 정보를 SecurityContextHolder에 설정하여 요청에 대한 접근 권한을 부여
 *
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());
    //pring Security의 필터 체인에서 주로 HTTP 기본 인증을 처리하는 BasicAuthenticationFilter를 상속
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // JWT 토큰 헤더를 추가하지 않아도 해당 필터는 통과는 할 수 있지만, 결국 시큐리티단에서 세션 값 검증에 실패함.
    //Authorization 헤더가 존재하고, 미리 정의된 접두사(예: Bearer )로 시작하는지 확인
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            // 토큰이 존재함
            log.debug("디버그 : 토큰이 존재함");
            //접두사를 제거하여 순수 JWT 토큰을 얻습니다.
            String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
            LoginUser loginUser = JwtProcess.verify(token); //JwtProcess.verify(token)를 사용하여 토큰을 검증하고 사용자 세부 정보를 가져옵
            log.debug("디버그 : 토큰이 검증이 완료됨");

            // 임시 세션 (UserDetails 타입 or username)
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                    loginUser.getAuthorities()); // id, role 만 존재
            SecurityContextHolder.getContext().setAuthentication(authentication); //Authentication 객체를 생성하여 SecurityContextHolder에 설정함으로써 Spring Security가 사용자를 인증된 것으로 인식
            log.debug("디버그 : 임시 세션이 생성됨");
        }
        chain.doFilter(request, response); //토큰의 존재 여부나 유효성과 관계없이 요청이 필터 체인을 통해 계속 진행
    }
 
    //헤더 검증메서드
    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVO.HEADER);
        if (header == null || !header.startsWith(JwtVO.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }
}