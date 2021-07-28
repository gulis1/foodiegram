package main.security;


import io.jsonwebtoken.*;
import main.persistence.entity.RoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtTokenFilter extends OncePerRequestFilter {

    private final String authSecret;

    public JwtTokenFilter(String authSecret) {
        this.authSecret = authSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            // Comprueba si se ha dado un token valido. Se lanza una UnsupportedJwtException si no es valido.

            String header = request.getHeader("Authorization");
            Claims claims = validateAuthToken(header);
            setUpSpringAuthentication(claims);

            chain.doFilter(request, response);



        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            response.setStatus(401);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private Claims validateAuthToken(String header) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {

        if (header == null || !header.startsWith("Bearer"))
            throw new MalformedJwtException("You need to log in to access this resource");

        return Jwts.parser().setSigningKey(authSecret.getBytes()).parseClaimsJws(header.substring(7)).getBody();
    }


    private void setUpSpringAuthentication(Claims claims) {

        List<RoleEnum> roles = new ArrayList<>();
        roles.add(RoleEnum.valueOf(claims.get("rol").toString()));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,roles );
        auth.setDetails(claims.get("username").toString());
        SecurityContextHolder.getContext().setAuthentication(auth);


    }
}
