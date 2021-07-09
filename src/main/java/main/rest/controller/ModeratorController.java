package main.rest.controller;

import main.application.service.ComentarioService;
import main.application.service.PublicationService;
import main.application.service.UserService;
import main.domain.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/mod")
public class ModeratorController {

    @Autowired
    private UserService service;

    @Autowired
    private PublicationService pubService;

    @Autowired
    private ComentarioService comService;



    @RequestMapping(value="/sendWarning",method=RequestMethod.POST)
    public ResponseEntity<?> sendWarning(@RequestPart ("user")String user,@RequestPart ("type")String type) {
    try{
        UserResource warning=service.sendWarning(user,Integer.parseInt(type));
        return ResponseEntity.ok(warning);
    }catch (IllegalArgumentException  e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    }
}
