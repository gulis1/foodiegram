package main.rest.controller;


import main.application.service.DiscoverService;
import main.domain.resource.PostPreview;
import main.domain.resource.RestaurantResource;
import main.domain.resource.UserPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/discover")
public class DiscoverController {

    @Autowired
    private DiscoverService service;


    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<?> discoverPosts(@RequestParam(required = false, defaultValue = "friends") String findBy,
                                           @RequestParam(required = false, defaultValue = "month") String period,
                                           @RequestParam(required = false, defaultValue = "%") String city,
                                           @RequestParam(required = false, defaultValue = "%") String country) {

        try {

            List<PostPreview> pub = null;

            switch (findBy) {

                case "friends": {
                    pub = service.discoverByAmigo();
                    break;
                }

                case "bestRated": {
                    pub = service.discoverBestRated(period, country, city);
                    break;
                }

                case "mostRated": {
                    pub = service.discoverMostRated(period, country, city);
                    break;
                }

            }


            return pub != null ? ResponseEntity.ok(pub) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> findFollowedByFriends() {

        try {
            Integer userid = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            List<UserPreview> users = service.findFollowedByFriends(userid);
            return users != null ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @RequestMapping(value = "/users/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> userWhoFollowXAlsoFollowY(@PathVariable String userName) {

        try {
            List<UserPreview> users = service.userWhoFollowXAlsoFollowY(userName);
            return users != null ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }




    @RequestMapping(value = "/collab", method = RequestMethod.GET)
    public ResponseEntity<?> findCollabbyLoc(@RequestParam(required = false, defaultValue = "%") String city,
                                             @RequestParam(required = false, defaultValue = "%") String country) {

        try {
            List<RestaurantResource> collabs = service.findCollabs(country, city);
            return collabs != null ? ResponseEntity.ok(collabs) : ResponseEntity.notFound().build();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

