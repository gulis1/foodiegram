package main.security;


import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.*;
import main.persistence.entity.RoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtTokenFilter extends OncePerRequestFilter {

    private final String authSecret;
    private final String loggedInSecret;

    public JwtTokenFilter(String authSecret, String loggedInSecret) {
        this.authSecret = authSecret;
        this.loggedInSecret = loggedInSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            // Comprueba si se ha dado un token valido. Se lanza una UnsupportedJwtException si no es valido.

            Cookie loggedInCookie = getCookie(request, "loggedIn");
            if (loggedInCookie == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new MalformedJwtException("You need to log in to access this resource");
            }


            Claims claims1 = checkLoggedInCookie(loggedInCookie);


            Cookie authCookie = getCookie(request, "authToken");

            if (authCookie == null)
                throw new MalformedJwtException("You need to log in to access this resource");

            Claims claims2 = validateAuthToken(authCookie);
            setUpSpringAuthentication(claims2);

            if (!claims1.getSubject().equals(claims2.getSubject()))
                throw new UnsupportedJwtException("Incompatible tokens.");

            chain.doFilter(request, response);



        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {

            UserAgent agent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
            BrowserType type = agent.getBrowser().getBrowserType();

            if (type == BrowserType.WEB_BROWSER || type == BrowserType.MOBILE_BROWSER || type == BrowserType.APP) {
                response.sendRedirect("/pruebas/refresh");
            }

            else {
                response.setStatus(401);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }

        }
    }

    private Claims checkLoggedInCookie(Cookie cookie) throws UnsupportedJwtException, MalformedJwtException {

        String jwToken = cookie.getValue();
        return Jwts.parser().setSigningKey(loggedInSecret.getBytes()).parseClaimsJws(jwToken).getBody();

    }

    private Claims validateAuthToken(Cookie cookie) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {

        String jwtToken = cookie.getValue();

        Claims claims =  Jwts.parser().setSigningKey(authSecret.getBytes()).parseClaimsJws(jwtToken).getBody();

        return claims;
    }


    private void setUpSpringAuthentication(Claims claims) {

        List<RoleEnum> roles = new ArrayList<>();
        roles.add(RoleEnum.valueOf(claims.get("rol").toString()));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,roles );
        auth.setDetails(claims.get("username").toString());
        SecurityContextHolder.getContext().setAuthentication(auth);


    }


    private Cookie getCookie(HttpServletRequest request, String name) {

        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies())
            if(cookie.getName().equals(name))
                return cookie;

        return null;
    }


}
