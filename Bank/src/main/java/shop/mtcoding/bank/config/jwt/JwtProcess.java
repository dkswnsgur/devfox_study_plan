package shop.mtcoding.bank.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

import java.util.Date;

public class JwtProcess {

    // 토큰 생성
    public static String create(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("bank") //토큰의 주제
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATTION_TIME)) //토큰의 만료시간
                .withClaim("id", loginUser.getUser().getId()) // 인스턴스 메서드로 변경
                .withClaim("role", loginUser.getUser().getRole() + "") //사용자 id를 클라임으로함
                .sign(Algorithm.HMAC512(JwtVO.SECRET)); // 대소문자 수정
        return JwtVO.TOKEN_PREFIX + jwtToken; //토큰 접두사(JwtVO.TOKEN_PREFIX)와 함께 완성된 토큰을 반환
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token); //JWT 라이브러리(Auth0 JWT)를 사용하여 토큰 검증을 위한 설정 HMAC512 알고리즘과 비밀 키(JwtVO.SECRET)를 사용하여 토큰의 서명을 검증
        Long id = decodedJWT.getClaim("id").asLong(); //토큰에 포함된 id 클레임을 추출하여 Long 타입으로 변환
        String role = decodedJWT.getClaim("role").asString(); //토큰에 포함된 role 클레임을 추출하여 String 타입으로 변환
        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build(); //build(): 설정을 완료하고 JWTVerifier 객체를 생성
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}