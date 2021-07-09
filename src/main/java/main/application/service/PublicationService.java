package main.application.service;

import main.domain.resource.CommentResource;
import main.domain.resource.PostResource;
import main.domain.resource.RatingResource;
import main.rest.forms.CommentForm;
import main.rest.forms.PostForm;
import main.rest.forms.RatingForm;

import javax.naming.NoPermissionException;
import java.io.IOException;
import java.util.List;

public interface
PublicationService {

    // POST

    // Devuelve la publicaion, o null si no existe.
    PostResource getPost(Integer pubID);

    // Para cambiar el texto lo la localizacion de una publicacion.
    // Devuelve la misma publicacion tras haberse aplicado el cambio.
    // Lanza excepcion si tanto text como loc son nulos.
    PostResource editPost(Integer pubID, String text) throws IllegalArgumentException,NoPermissionException;

    // Elimina una publicaicon.
    // Devuelve la publicacion eliminada, o null si no existe.
    PostResource deletePost(Integer pubID) throws NoPermissionException;

    // Sube una publicacion. Devuelve la propia publicacion si ha habido exito, null si el usuario no existe.
    // Lanza excepcion si el usuario no existe, o si no se puede guardar la imagen.
    // Con este vais a tener problemas haciendo pruebas, yo pasaria de él.
    PostResource upload(Integer userID, PostForm form) throws IOException, IllegalArgumentException;


    // VALORACIONES

    // Devuelve una lista de ValoracionResource con todas las valoraciones de una publicacion, retorna null si la publicacion no existe.
    List<RatingResource> getRatings(Integer pubID);

    // Devuelve un ValoracionResource con la valoracion posteada, salta una exception si el usuario no existe o si el la puntuacion es menor que 0 y mayor que 5.
    RatingResource setRating(RatingForm form) throws IllegalArgumentException;

    //Devuelve  un ValoracionResource con la valoracion de un usuario dentro de una publicacion, retorna null si el usuario no existe.
    RatingResource getRating(Integer pubID, String user) ;

    //Devuelve un ValoracionResource con la valoracion eliminada,retorna null si el usuario no existe.
    RatingResource deleteRating(RatingForm form);

    // COMENTARIOS

    // Devuelve la lista de comentarios de una foto, o null si la publicacion no existe.
    List<CommentResource> getComments(Integer pubID);

    // Añade o cambia un el comentario de un usuario en una publicacion.
    // Devuelve el mismo comentario tras haberse aplicado el cambio.
    // Lanza excepcion si el texto es null o vacio, o si el usuario no existe.
    CommentResource setComment(CommentForm form) throws IllegalArgumentException;






}
