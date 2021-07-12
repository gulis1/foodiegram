package main.rest.controller;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import main.application.service.UserService;
import main.domain.resource.PostPreview;
import main.domain.resource.UserResource;
import main.domain.resource.RatingResource;
import main.rest.forms.UserForm;
import main.security.AuthTokenGenerator;
import main.security.LoggedInTokenGenerator;
import main.security.RefreshTokenGenerator;
import main.security.TokenRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Value("${domain}")
    private String domain;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private RefreshTokenGenerator refreshTokenGenerator;

    @Autowired
    private LoggedInTokenGenerator loggedInTokenGenerator;

    @Autowired
    private TokenRefresher tokenRefresher;

    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByName(@PathVariable String user, @RequestParam(required = false) String id) {

        UserResource usuario = service.getUserByName(user);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // Si buscas /users/user/postID se te redirige a /posts/postID
    @RequestMapping(value = "/{user}/{pubID}", method = RequestMethod.GET)
    public void redirectToPost(HttpServletResponse response, @PathVariable Integer pubID) throws IOException {
        response.sendRedirect("/posts/" + pubID);
    }

    // Devuelve una lista con todas las IDs de las publicaciones del usuario y las imagenes correspondientes.
    @RequestMapping(value = "/{user}/posts", method = RequestMethod.GET)
    public ResponseEntity<List<PostPreview>> getPosts(@PathVariable String user) {

        List<PostPreview> publicaciones = service.getPosts(user);
        return publicaciones != null ? ResponseEntity.ok(publicaciones) : ResponseEntity.notFound().build();
    }


    //no se si lo llegaremos a usar
    @RequestMapping(value = "/{user}/ratings", method = RequestMethod.GET)
    public ResponseEntity<List<RatingResource>> getRatingsUser(@PathVariable String user) {

        List<RatingResource> valoraciones = service.getRatings(user);
        return valoraciones != null ? ResponseEntity.ok(valoraciones) : ResponseEntity.notFound().build();

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(UserForm user) {

        try {
            UserResource newUser = service.register(user);
            return ResponseEntity.ok(newUser);
        }

        catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid form.");
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @RequestMapping(value = "verify/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> verifyUser(@PathVariable Integer token) {

        try {

            UserResource newUser = service.verify(token);
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> login(UserForm user, HttpServletResponse response) {

        try {
            UsernamePasswordAuthenticationToken userData = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authenticationManager.authenticate(userData);
            // Generamos el token de autentificacion
            String authToken = authTokenGenerator.buildToken(user.getUsername(), 15);
            Cookie cookieA = new Cookie("authToken",authToken);
            cookieA.setDomain(domain);
            cookieA.setHttpOnly(true);
            cookieA.setMaxAge(900);
            cookieA.setPath("/");

            response.addCookie(cookieA);

            // Generamos el refresh token
            String refreshToken = refreshTokenGenerator.buildToken(user.getUsername(), 300);
            Cookie cookieR = new Cookie("refreshToken", refreshToken);
            cookieR.setDomain(domain);
            cookieR.setHttpOnly(true);
            cookieR.setMaxAge(18000);
            cookieR.setPath("/users/refresh");

            response.addCookie(cookieR);


            String loginToken = loggedInTokenGenerator.getToken(user.getUsername(), 300);

            Cookie loggedInCookie = new Cookie("loggedIn", loginToken);
            loggedInCookie.setPath("/");
            loggedInCookie.setMaxAge(18000);

            response.addCookie(loggedInCookie);



            return ResponseEntity.ok(" {'Status' : '200'} ");
        }

        catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("{'Status' : 400, 'message': '%s' }",e.getMessage()));
        }

        catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{'Status' : 401, 'message': 'Wrong Credentials' }");
        }

        catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{'Status' : 401, 'message': 'User is Disabled' }");
        }
    }

    @RequestMapping(value="/refresh", method=RequestMethod.GET)
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {

        try {
            String token = tokenRefresher.refresh(refreshToken);
            Cookie cookieA = new Cookie("authToken",token);
            cookieA.setDomain(domain);
            cookieA.setHttpOnly(true);
            cookieA.setMaxAge(900);
            cookieA.setPath("/");
            response.addCookie(cookieA);

            return ResponseEntity.ok(" {'Status' : '200'} ");
        }

        catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{'Status' : 401, 'message': 'Token has expired }");
        }

        catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{'Status' : 401, 'message': 'Malform Token ' }");
        }



    }



}

