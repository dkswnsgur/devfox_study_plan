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
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATTION_TIME))
                .withClaim("id", loginUser.getUser().getId()) // 인스턴스 메서드로 변경
                .withClaim("role", loginUser.getUser().getRole() + "")
                .sign(Algorithm.HMAC512(JwtVO.SECRET)); // 대소문자 수정
        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}