package main.rest.controller;


import main.application.service.DiscoverService;
import main.application.service.MensajeService;
import main.domain.resource.MensajeResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PublicacionResource;
import main.domain.resource.UsuarioResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/discover")
public class ControllerDescubrir {

    @Autowired
    private DiscoverService service;

    @RequestMapping(value="/posts/",method = RequestMethod.GET)
    public ResponseEntity<?> discoverPosts(@RequestParam(required = false) String findBy,
                                           @RequestParam(required = false, defaultValue = "month") String period){

        try {
            Integer userid=Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            List<PreviewPublicacion> pub = null;

            switch(findBy) {

                case "friends": {
                    pub = service.discoverByAmigo(userid);
                    break;
                }

                case "bestRated": {
                    pub = service.discoverBestRated(period);
                    break;
                }

                case "mostRated": {
                    pub = service.discoverMostRated(period);
                    break;
                }

            }


            ;

            return pub != null ? ResponseEntity.ok(pub) : ResponseEntity.notFound().build();
        }

        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }



    @RequestMapping(value="/users/xd",method = RequestMethod.GET)
    public ResponseEntity<?> findFollowedByFriends(){

        try{
            Integer userid=Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            List<UsuarioResource> users = service.findFollowedByFriends(userid);
            return users != null ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }



}



