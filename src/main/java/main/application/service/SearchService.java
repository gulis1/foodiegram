package main.application.service;

import main.domain.resource.PostPreview;
import main.domain.resource.RestaurantResource;
import main.domain.resource.UserPreview;

import java.util.List;

public interface SearchService {

    // BUSQUEDA DE USUARIOS
    //
    // devuelve una lista de usuarios cuyo nombre contenga username
    List<UserPreview> getUserList(String username);

    // devuelve una lista de usuarios por numero de publicaciones
    List<UserPreview> getUserListByPubli();

    // devuelve una lista de usuarios por numero de valoraciones recibidas
    List<UserPreview> getUserListByVal();

    // BUSQUEDA DE LOCALES
    //
    // devuelve una lista de colaboradores cuyo nombre contenga colabname
    List<RestaurantResource> getColabListByName(String colabname);

    // devuelve una lista de colaboradores cuyo type contenga type
    List<RestaurantResource> getColabListByType(String type);

    // BUSQUEDA DE PUBLICACIONES
    //
    // devuelve una lista de publicaciones cuyo texto contenga un hashtag coincidente con tag
    List<PostPreview> getPubliListByTag(String tag);
}
