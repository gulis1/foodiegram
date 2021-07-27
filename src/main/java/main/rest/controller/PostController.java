package main.rest.controller;


import main.application.service.PublicationService;
import main.domain.resource.CommentResource;
import main.domain.resource.PostResource;
import main.domain.resource.RatingResource;
import main.rest.forms.CommentForm;
import main.rest.forms.PostForm;
import main.rest.forms.RatingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {

    @Autowired
    private PublicationService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> upload(@Valid PostForm form) {


        try {
            Integer userid = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            PostResource publi = service.upload(userid, form);
            return publi != null ? ResponseEntity.ok(publi) : ResponseEntity.notFound().build();
        }

        catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("F");
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @RequestMapping(value="/{pubID}", method = RequestMethod.GET)
    public ResponseEntity<PostResource> getPost(@PathVariable Integer pubID) {

        PostResource publi =  service.getPost(pubID);
        return publi != null ? ResponseEntity.ok(publi) : ResponseEntity.notFound().build();

    }

    @RequestMapping(value="/{pubID}", method = RequestMethod.PUT)
    ResponseEntity<?> edit(@PathVariable Integer pubID, @RequestPart(value = "text", required = false) String text) {

        try {
            PostResource publi = service.editPost(pubID, text);
            return publi != null ? ResponseEntity.ok(publi) : ResponseEntity.notFound().build();
        }

        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (NoPermissionException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

    @RequestMapping(value="/{pubID}",method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Integer pubID) {
        try {
            PostResource publi = service.deletePost(pubID);
            return publi != null ? ResponseEntity.ok(publi) : ResponseEntity.notFound().build();
        }

        catch (NoPermissionException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }


    }

    //devuleve un JSON con todas la valoraciones de una publicacion
    @RequestMapping(value = "/{pubID}/ratings", method = RequestMethod.GET)
    public ResponseEntity<List<RatingResource>> getRatings(@PathVariable Integer pubID) {

        List<RatingResource> valoraciones = service.getRatings(pubID);
        return valoraciones != null ? ResponseEntity.ok(valoraciones) : ResponseEntity.notFound().build();

    }

    //setea o updatea la valoracion de un usuario en una publicacion
    @RequestMapping(value="/{pubID}/ratings", method = RequestMethod.POST)
    public ResponseEntity<?> setRating(RatingForm form, @PathVariable Integer pubID){

        try {
            Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            form.setUserID(userID);
            form.setPubID(pubID);
            RatingResource valoracion = service.setRating(form);
            return ResponseEntity.ok(valoracion);
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

   //devuele si un usuario a valorado una publicacion
   @RequestMapping(value="/{pubID}/ratings/me", method = RequestMethod.GET)
    public ResponseEntity<RatingResource> getRating(@PathVariable Integer pubID){
        System.out.println("hey2");
        String user = SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
        RatingResource val=  service.getRating(pubID, user);

        return val != null ? ResponseEntity.ok(val) : ResponseEntity.notFound().build();

    }

    //borra una valoracion dentro una publicacion de un usuario
    @RequestMapping(value = "/{pubID}/ratings", method=RequestMethod.DELETE)
    public ResponseEntity<RatingResource> deleteRating(RatingForm form, @PathVariable Integer pubID){

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        form.setUserID(userID);
        form.setPubID(pubID);
        RatingResource val = service.deleteRating(form);
        return val != null ? ResponseEntity.ok(val) : ResponseEntity.notFound().build();

    }


    @RequestMapping(value="/{pubID}/comments", method=RequestMethod.GET)
    public ResponseEntity<List<CommentResource>> getComments(@PathVariable Integer pubID) {

        List<CommentResource> comentarios = service.getComments(pubID);
        return comentarios != null ? ResponseEntity.ok(comentarios) : ResponseEntity.notFound().build();

    }


    @RequestMapping(value = "/{pubID}/comments",method = RequestMethod.POST)
    public ResponseEntity<?> setComment(@PathVariable Integer pubID, CommentForm form){

        try{

            Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            form.setUserID(userID);
            form.setPubID(pubID);
            CommentResource comment = service.setComment(form);
            return ResponseEntity.ok(comment);
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        
    }

}