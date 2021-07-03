package main.application.service;


import main.domain.resource.ColaboradorResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PreviewUsuario;

import java.util.List;

public interface DiscoverService {

    // Devuelve una lista de Previewpublicaciones de las publicaciones de tus amigos.
    List<PreviewPublicacion> discoverByAmigo();

    // Devuelve una lista de Previewpublicaciones con las publicaciones mejores valoradas.
    List<PreviewPublicacion> discoverBestRated(String period, String country, String city) throws IllegalArgumentException;

    // Devuelve una lista de Previewpublicaciones con las publicaciones mas valoradas.
    List<PreviewPublicacion> discoverMostRated(String period, String country, String city) throws IllegalArgumentException;




    // Devuelve una lista de PreviewUsuarios con los usuarios de las personas que siguen tus amigos. (Maximo 50)
    List<PreviewUsuario> findFollowedByFriends(Integer userid);

    // Devuelve una lista de PreviewUsuario de los usuarios mas comunes a los que tambien sigue la gente que sigue a un usuario. (Maximo 10)
    List<PreviewUsuario> userWhoFollowXAlsoFollowY(String userName);



    //devuelve una lista con los PreviewColabJOINUser que esten en el mismo pais y/o ciudad ordenados por VIP.
    List<ColaboradorResource> findCollabs(String country, String city);

}
