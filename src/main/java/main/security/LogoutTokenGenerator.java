package main.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.persistence.entity.User;
import main.persistence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogoutTokenGenerator {

    @Value("${jwt.logout.secret}")
    private String secretKey;

    @Autowired
    private UserRepo repo;

    public String getToken(String username, Integer minutes) {

        minutes *= 60000;
        User user = repo.getByName(username);
        
        return  Jwts.builder()
                .setSubject(user.getUserid().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + minutes))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
    }
}
