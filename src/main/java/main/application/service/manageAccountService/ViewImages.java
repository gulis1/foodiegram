package main.application.service.manageAccountService;

import main.domain.resource.PostPreview;

import java.util.List;

public interface ViewImages {

    //Retorna la lista de publicaciones del usuario idUser, devuelve null en caso de no encontrar el usuario idUser
    List<PostPreview> viewPost(Integer idUser);

}
