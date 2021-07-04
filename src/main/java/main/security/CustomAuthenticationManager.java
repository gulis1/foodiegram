package main.security;


import main.persistence.entity.User;
import main.persistence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    UserRepo userRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException, DisabledException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Optional<User> usuario = userRepo.findByName(username);

        if (!usuario.isPresent() || !encoder.matches(password, usuario.get().getPasswd()))
            throw new BadCredentialsException("1000");

        else if (!usuario.get().isEnabled())
            throw new DisabledException("1001");

        else
            return new UsernamePasswordAuthenticationToken(usuario.get().getUserid(), password, null);


    }
}
