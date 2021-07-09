package main.rest.controller;

import main.application.service.ComentarioService;
import main.domain.resource.CommentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

import javax.naming.NoPermissionException;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/comments/{comID}")
public class CommentController {

    @Autowired
    ComentarioService service;

    //edita el comentario segun el comID (modifica solo el texto)
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> editComment(@PathVariable Integer comID, @RequestPart(value="text") String text){

       try{
           CommentResource comment = service.editComentario(comID, text);
           return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
       }

       catch (NoPermissionException e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
       }
       catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }

    }

    //borra el comentario segun el comID
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable Integer comID) {

        try{
            CommentResource comment = service.deleteComentario(comID);
            return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
        }

        catch (NoPermissionException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }


    }


}


