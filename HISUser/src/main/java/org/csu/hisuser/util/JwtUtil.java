package org.csu.hisuser.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 3;   //三天
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    //生成token
    public String generateToken(String username,int userCategory) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userCategory", userCategory)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //解析token获取用户名
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    //解析token 获得权限等级
    public Integer extractRoleLevel(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userCategory", Integer.class);  // 获取 int 类型的 roleLevel
        } catch (Exception e) {
            return null;
        }
    }

    //验证token是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);;
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
