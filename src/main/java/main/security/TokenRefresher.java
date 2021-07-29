package main.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenRefresher {

    @Value("${jwt.refresh.secret}")
    private String secretKey;

    @Autowired
    AuthTokenGenerator generator;

    public String refresh(String token) throws ExpiredJwtException, MalformedJwtException {

        Claims claims =  Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        return generator.buildToken(Integer.parseInt(claims.getSubject()),  30);
    }

}
