package main.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import main.persistence.entity.Refreshtoken;
import main.persistence.repository.RefreshtokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenRefresher {

    @Value("${jwt.refresh.secret}")
    private String secretKey;

    @Autowired
    RefreshtokenRepo repo;

    @Autowired
    AuthTokenGenerator generator;

    public String refresh(String token) throws ExpiredJwtException, MalformedJwtException {

        Claims claims =  Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        Optional<Refreshtoken> lastToken = repo.findByUser(Integer.parseInt(claims.getSubject()));

        if (lastToken.isPresent() && claims.getExpiration().compareTo(lastToken.get().getExpiredate()) < 0)
            throw new ExpiredJwtException(null, claims, "A new token for this user has been created");

        return generator.buildToken(Integer.parseInt(claims.getSubject()),  30);



    }

}
