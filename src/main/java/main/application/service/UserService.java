package main.application.service;

import main.domain.resource.PostPreview;
import main.domain.resource.RatingResource;
import main.domain.resource.UserResource;
import main.domain.resource.Usuario_baneadoResource;
import main.rest.forms.UserForm;

import java.util.List;

public interface UserService  {

    UserResource getUser(Integer userID);

    // Devuelve la info del usuario, o null si el usuario no existe.
    UserResource getUserByName(String user);

    // Devuelve una lista con todas las publicaciones (id e imagen) del usuario, o null si el usuario no existe.
    List<PostPreview> getPosts(String user);

    // Devuelve todoas las valoraciones que ha hecho un usuario (En todas las publicaciones), o null si el usuario no existe.
    List<RatingResource> getRatings(String user);

    // Da de alta un usuario en la BD y le envia un mail de confirmacion.
    // Lanza una excepcion si se le introduce un usuario, contraseña o email no validos.
    // Si se ha podido registrar al usuario, lo devuelve.
    UserResource register(UserForm user) throws IllegalArgumentException;

    // Sudad de este.
    UserResource verify(Integer token);

    //banea por nombre y una severidad de 1 a 5 siendo 5 50años
    Usuario_baneadoResource banUser(String user, String severity);

    //desbanea usuario por nombre
    Usuario_baneadoResource unbanUser(String user);

    //elimina usuario por nombre
    UserResource deleteUser(String user);

    //devuelve la lista de usuarios baneados
    List<Usuario_baneadoResource> getBannedUserList();

    //envia un mensaje por correo con la advertencia.
    UserResource sendWarning(String user, Integer type);
}
