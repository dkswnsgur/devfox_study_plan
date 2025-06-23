package com.example.demo.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jwtService")
public class JwtServiceImpl implements JwtService {

    private String secretKey = "abbbic2q35468@@@54954fsfsf!!asfeefsfsfefsfefwgwgwghe";

    @Override
    public String getToken(String key, Object value) {

        // 토큰 만료 시간 설정 (5분 후)
        Date expTime = new Date(System.currentTimeMillis() + 1000 * 60 * 5);

        // 시크릿 키를 바이트 배열로 변환
        byte[] secretByteKey = secretKey.getBytes(); // 또는 Base64 처리 원할 시 Base64.getDecoder() 등 사용

        // 서명 키 생성
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        // 헤더 설정
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        // Claims 설정
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        // JWT 빌드
        JwtBuilder builder = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    @Override
    public Claims getClaims(String token) {
        if (token != null && !"".equals(token))
        {
            try {
                byte[] secretByteKey = secretKey.getBytes();
                Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
                Claims claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
                return claims;
            } catch (ExpiredJwtException e){
                // 만료됨
            } catch (JwtException e) {
                // 유효하지 않음
            }
        }


        return null;
    }

    @Override
    public boolean isValid(String token) {
        return this.getClaims(token) != null;
    }

    @Override
    public int getId(String token) {
        Claims claims = this.getClaims(token);

        if (claims != null) {
            return Integer.parseInt(claims.get("id").toString());
        }

        return 0;
    }
}