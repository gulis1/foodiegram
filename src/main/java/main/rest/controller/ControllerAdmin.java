package main.rest.controller;

import main.application.service.UserService;
import main.domain.resource.UserResource;
import main.domain.resource.Usuario_baneadoResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/admin")
public class ControllerAdmin {

    @Autowired
    private UserService service;

  

    @RequestMapping(value="/ban",method=RequestMethod.POST)
    public ResponseEntity<?> banUser(@RequestPart("user") String user,@RequestPart("severity") String severe) {
        try{
            Usuario_baneadoResource bannedUser=service.banUser(user,severe);
            return ResponseEntity.ok(bannedUser);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value="/unban",method=RequestMethod.POST)
    public ResponseEntity<?> unbanUser(@RequestPart("user") String user) {
        try{
            Usuario_baneadoResource bannedUser=service.unbanUser(user);
            return ResponseEntity.ok(bannedUser);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value="/deleteUser",method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestPart("user") String user) {
        try{
            UserResource bannedUser=service.deleteUser(user);
            return ResponseEntity.ok(bannedUser);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value="/banlist",method = RequestMethod.GET)
    public ResponseEntity<?> getBannedUser() {

        List<Usuario_baneadoResource> bulist = service.getBannedUserList();
        return bulist !=  null ? ResponseEntity.ok(bulist) : ResponseEntity.notFound().build();

    }

}
