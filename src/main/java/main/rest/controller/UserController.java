package main.rest.controller;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import main.application.service.UserService;
import main.domain.resource.LoginResource;
import main.domain.resource.PostPreview;
import main.domain.resource.RatingResource;
import main.domain.resource.UserResource;
import main.rest.forms.UserForm;
import main.security.AuthTokenGenerator;
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
    public ResponseEntity<?> login(UserForm user) {

        try {
            UsernamePasswordAuthenticationToken userData = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authenticationManager.authenticate(userData);

            // Generamos el token de autentificacion
            String authToken = authTokenGenerator.buildToken(user.getUsername(), 15);

            // Generamos el refresh token
            String refreshToken = refreshTokenGenerator.buildToken(user.getUsername(), 300);

            LoginResource resource = new LoginResource(authToken, refreshToken, null);

            return ResponseEntity.ok(resource);
        }

        catch (NullPointerException e) {
            LoginResource resource = new LoginResource(null, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resource);
        }

        catch (BadCredentialsException ex) {
            LoginResource resource = new LoginResource(null, null, "Wrong credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }

        catch (DisabledException e) {
            LoginResource resource = new LoginResource(null, null, "User is disabled.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }
    }

    @RequestMapping(value="/refresh", method=RequestMethod.GET)
    public ResponseEntity<?> refresh(@RequestParam("refreshToken") String refreshToken) {

        try {
            String token = tokenRefresher.refresh(refreshToken);
            LoginResource resource = new LoginResource(token, null, null);
            return ResponseEntity.ok(resource);
        }

        catch (ExpiredJwtException e) {
            LoginResource resource = new LoginResource(null, null, "Expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }

        catch (MalformedJwtException e) {
            LoginResource resource = new LoginResource(null, null, "Malformed token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }



    }



}

