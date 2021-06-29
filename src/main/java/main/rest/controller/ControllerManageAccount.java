package main.rest.controller;

import main.application.service.UserService;
import main.application.service.manageAccountService.ManageFriends;
import main.application.service.manageAccountService.ManageInfo;
import main.application.service.manageAccountService.ViewImages;
import main.domain.resource.AmigoResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.UsuarioResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/manage_account")
public class ControllerManageAccount {

    @Autowired
    ManageFriends manageFriends;

    @Autowired
    ManageInfo manageInfo;

    @Autowired
    UserService userService;

    @Autowired
    ViewImages viewImagesService;

    //--MANAGE FRIENDS--

    //añade un amigo pasando un id de la persona que añade amigo y el nombre de la persona que quiere añadir
    @RequestMapping(method = RequestMethod.PUT)

    public ResponseEntity<?> add(@RequestPart(value = "name", required = true) String name)throws IllegalArgumentException{
        try{
            Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            AmigoResource friend = manageFriends.addFriend(userID, name);
            return friend != null ? ResponseEntity.ok(friend) : ResponseEntity.notFound().build();
        }

        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    //elimina un amigo pasando un id de la persona que elimina amigo y el nombre de la persona que quiere eliminar
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> remove(@RequestPart(value = "name", required = true) String name)throws IllegalArgumentException{
        try{
            Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            AmigoResource friend = manageFriends.removeFriend(userID, name);
            return friend != null ? ResponseEntity.ok(friend) : ResponseEntity.notFound().build();
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    //permite ver las imagenes del usuario con nombre name si el amigo del usuario con id
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PreviewPublicacion>> viewImagesofFriend(@RequestPart(value = "name", required = true) String name){

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        List<PreviewPublicacion> publicacionesAmigo = userService.getPosts(name);
        return publicacionesAmigo != null ? ResponseEntity.ok(publicacionesAmigo) : ResponseEntity.notFound().build();
    }
    //-------------------------------------------------------------------------------------------------------------------------

    //--MANAGE INFO--

    //permite cambiar el nombre de la persona con id si este nombre no esta cogido por otro usuario
    @RequestMapping(value = "/newName", method = RequestMethod.POST)
    public ResponseEntity<?> changeName(@RequestPart(value = "newName", required = true) String newName) {

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        UsuarioResource user = manageInfo.changeName(userID, newName);
        return user!= null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //permite cambiar la contraseña del usuario con id
    @RequestMapping(value ="/newPasswd", method = RequestMethod.POST)
    public ResponseEntity<?> changePasswd(@RequestPart(value = "newPasswd", required = true) String newPasswd) {

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        UsuarioResource user = manageInfo.changePasswd(userID, newPasswd);
        return user!= null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //permite cambiar el email del usuario con id
    @RequestMapping(value = "/newEmail", method = RequestMethod.POST)
    public ResponseEntity<?> changeEmail(@RequestPart(value = "newEmail", required = true) String newEmail) {

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        UsuarioResource user = manageInfo.changeEmail(userID, newEmail);
        return user!= null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //permite cambiar la foto de perfil del usuario con id
    @RequestMapping(value = "/newPic", method = RequestMethod.POST)
    public ResponseEntity<?> changeProfilePic(@RequestPart("image") MultipartFile image) {

        try {
            Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            UsuarioResource user = manageInfo.changeProfilePicture(userID, image);
            return user!= null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    //-------------------------------------------------------------------------------------------------------------------------

    //--UNSUBSCRIBE--

    //permite darte de baja de la aplicación
    @RequestMapping(value  = "/unsubscribe",method = RequestMethod.DELETE)
    public ResponseEntity<?> unsubscribe() {

        String userName = SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
        UsuarioResource user = userService.deleteUser(userName);
        return user!= null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //-------------------------------------------------------------------------------------------------------------------------

    //--VIEW MY IMAGES--

    //permite ver tus publicaciones
    @RequestMapping(value = "/viewImage", method = RequestMethod.GET)
    public ResponseEntity<List<PreviewPublicacion>> viewMyImages() {

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        List<PreviewPublicacion> _listPost = viewImagesService.viewPost(userID);
        return _listPost != null ? ResponseEntity.ok(_listPost) : ResponseEntity.notFound().build();
    }
    //-------------------------------------------------------------------------------------------------------------------------

}
