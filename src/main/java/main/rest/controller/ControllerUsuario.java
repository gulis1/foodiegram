package main.rest.controller;


import main.application.service.UserService;
import main.domain.resource.*;
import main.persistence.entity.RoleEnum;
import main.security.JWTokenGenerator;
import main.security.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/users")
public class ControllerUsuario {

    @Autowired
    private UserService service;

    @Value("${direccion}")
    private String direccionWeb;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTokenGenerator jwtGenerator;

       @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByName(@PathVariable String user) {

        UsuarioResource usuario = service.getUserByName(user);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // Si buscas /users/user/postID se te redirige a /posts/postID
    @RequestMapping(value = "/{user}/{pubID}", method = RequestMethod.GET)
    public void redirectToPost(HttpServletResponse httpServletResponse, @PathVariable String user, @PathVariable Integer pubID) {

        httpServletResponse.setHeader("Location", direccionWeb + "/posts/" + pubID);
        httpServletResponse.setStatus(302);
    }

    // Devuelve una lista con todas las IDs de las publicaciones del usuario y las imagenes correspondientes.
    @RequestMapping(value = "/{user}/posts", method = RequestMethod.GET)
    public ResponseEntity<List<PreviewPublicacion>> getPosts(@PathVariable String user) {

        List<PreviewPublicacion> publicaciones = service.getPosts(user);
        return publicaciones != null ? ResponseEntity.ok(publicaciones) : ResponseEntity.notFound().build();
    }


    //no se si lo llegaremos a usar
    @RequestMapping(value = "/{user}/ratings", method = RequestMethod.GET)
    public ResponseEntity<List<ValoracionResource>> getRatingsUser(@PathVariable String user) {

        List<ValoracionResource> valoraciones = service.getRatings(user);
        return valoraciones != null ? ResponseEntity.ok(valoraciones) : ResponseEntity.notFound().build();

    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute("employee") UserForm user) {

        try {
            UsuarioResource newUser = service.register(user.getUsername(), user.getPassword(), user.getEmail());
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

            UsuarioResource newUser = service.verify(token);
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<String> login(@Valid @ModelAttribute("employee") UserForm user, HttpServletResponse response) {

        try {
            UsernamePasswordAuthenticationToken userData = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authenticationManager.authenticate(userData);
            // Generamos el token de autentificacion
            String authToken = jwtGenerator.buildToken(user.getUsername(), 15);
            // Generamos el refresh token
            String refreshToken = jwtGenerator.buildToken(user.getUsername(), 300);
            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setDomain(direccionWeb);
            cookie.setMaxAge(18000);
            cookie.setPath("/users/refresh");

            response.addCookie(cookie);
            return ResponseEntity.ok(String.format("{\"status\": \"200\", \"token\": \"%s\"}", authToken));
        }

        catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid form.");
        }

        catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials.");
        }

        catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is disabled.");
        }
    }


}

