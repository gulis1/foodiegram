package main.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.persistence.entity.User;
import main.persistence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenGenerator {

    @Autowired
    UserRepo repoUser;

    @Value("${jwt.refresh.secret}")
    private String secretKey;

    public String buildToken(String username, int minutes) {

        Optional<User> user = repoUser.findByName(username);

        if (!user.isPresent())
            return null;

        minutes *= 60000;

        return Jwts.builder()
                .setSubject(user.get().getUserid().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + minutes))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
    }

}
