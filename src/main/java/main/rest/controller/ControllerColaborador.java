package main.rest.controller;

import main.application.service.ColaboradorService;
import main.domain.resource.ColaboradorResource;
import main.domain.resource.EventoResource;
import main.rest.forms.CollaborateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/collab")
public class ControllerColaborador {

    @Autowired
    private ColaboradorService serviceC;


    @GetMapping
    public ResponseEntity<?> get(){

        try{
            Integer userid=Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            ColaboradorResource colab = serviceC.getCollab(userid);
            return colab != null ? ResponseEntity.ok(colab) : ResponseEntity.notFound().build();
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @RequestMapping(value="/upgrade",method = RequestMethod.POST)
    public ResponseEntity<?> upgrade(CollaborateForm form){

        try{
            Integer userid=Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            ColaboradorResource colab= serviceC.upgradeUser(userid,form);
            return colab != null ? ResponseEntity.ok(colab) : ResponseEntity.notFound().build();
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
