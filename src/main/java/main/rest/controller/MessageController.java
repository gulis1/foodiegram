package main.rest.controller;


import main.application.service.MensajeService;
import main.domain.resource.MessageResource;
import main.rest.forms.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.naming.NoPermissionException;
import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MensajeService service;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getMensajes() {

        Integer userid = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        List<MessageResource> mens = service.getMensajes(userid);
        return mens !=  null ? ResponseEntity.ok(mens) : ResponseEntity.notFound().build();

    }

    //hace un upload con el mensaje
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> setMensaje(MessageForm form){

        try {
            Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            MessageResource mens = service.setMensaje(userID, form);
            return ResponseEntity.ok(mens);
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @RequestMapping(value="/{mensID}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMensaje(@PathVariable Integer mensID) {

        try {
            MessageResource mens = service.deleteMensaje(mensID);
            return mensID != null ? ResponseEntity.ok(mens) : ResponseEntity.notFound().build();
        }

        catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }



    }

}



