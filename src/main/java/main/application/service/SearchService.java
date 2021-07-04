package main.application.service;

import main.domain.resource.ColaboradorResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PreviewUsuario;

import java.util.List;

public interface SearchService {

    // BUSQUEDA DE USUARIOS
    //
    // devuelve una lista de usuarios cuyo nombre contenga username
    List<PreviewUsuario> getUserList(String username);

    // devuelve una lista de usuarios por numero de publicaciones
    List<PreviewUsuario> getUserListByPubli();

    // devuelve una lista de usuarios por numero de valoraciones recibidas
    List<PreviewUsuario> getUserListByVal();

    // BUSQUEDA DE LOCALES
    //
    // devuelve una lista de colaboradores cuyo nombre contenga colabname
    List<ColaboradorResource> getColabListByName(String colabname);

    // devuelve una lista de colaboradores cuyo type contenga type
    List<ColaboradorResource> getColabListByType(String type);

    // BUSQUEDA DE PUBLICACIONES
    //
    // devuelve una lista de publicaciones cuyo texto contenga un hashtag coincidente con tag
    List<PreviewPublicacion> getPubliListByTag(String tag);
}
