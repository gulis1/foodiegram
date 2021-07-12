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
public class AuthTokenGenerator {

    @Autowired
    private UserRepo repoUser;


    @Value("${jwt.auth.secret}")
    private String secretKey;


    public String buildToken(String username, int minutes) {

        User user = repoUser.getByName(username);
        return getToken(user.getUserid(), username, minutes, user.getRole().toString());
    }

    public String buildToken(Integer userID, int minutes) {

        User user = repoUser.getById(userID);
        return getToken(user.getUserid(), user.getName(), minutes, user.getRole().toString());
    }

    private String getToken(Integer userID, String userName, int minutes, String role) {

        minutes *= 60000;

        return  Jwts.builder()
                .setSubject(userID.toString())
                .claim("username", userName)
                .claim("rol", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + minutes))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
    }

}

