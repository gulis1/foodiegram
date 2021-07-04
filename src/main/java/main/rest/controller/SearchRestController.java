package main.rest.controller;

import main.application.service.SearchService;
import main.domain.resource.ColaboradorResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PreviewUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/search")
public class SearchRestController {

    @Autowired
    private SearchService service;

    // BUSQUEDA DE USUARIOS
    //
    // busca a usuarios cuyo name contenga el string "username" y devuelve una lista
    // con los nombres de usuario y su foto de perfil
    // si no encuentra devuelve una lista vacia
    @RequestMapping(value = "/users/name/{username}", method = RequestMethod.GET)
    public ResponseEntity<List<PreviewUsuario>> getUserList(@PathVariable String username) {

        List<PreviewUsuario> userList = service.getUserList(username);
        return userList != null ? ResponseEntity.ok(userList) : ResponseEntity.notFound().build();
    }

    // busca a usuarios por mayor numero de publicaciones y devuelve una lista (max 15)
    // con los nombres de usuario y su foto de perfil
    // si no encuentra devuelve una lista vacia
    @RequestMapping(value = "/users/publi", method = RequestMethod.GET)
    public ResponseEntity<List<PreviewUsuario>> getUserListByPubli() {

        List<PreviewUsuario> userList = service.getUserListByPubli();
        return userList != null ? ResponseEntity.ok(userList) : ResponseEntity.notFound().build();
    }

    // busca a usuarios por mayor numero de valoracionres recibidas y devuelve una lista (max 15)
    // con los nombres de usuario y su foto de perfil
    // si no encuentra devuelve una lista vacia
    @RequestMapping(value = "/users/pval", method = RequestMethod.GET)
    public ResponseEntity<List<PreviewUsuario>> getUserListByVal() {

        List<PreviewUsuario> userList = service.getUserListByVal();
        return userList != null ? ResponseEntity.ok(userList) : ResponseEntity.notFound().build();
    }

    // BUSQUEDA DE LOCALES
    //
    // busca a colaboradores cuyo name contenga el string "colabname" y devuelve una lista
    // con los datos de colaborador y su foto de perfil
    // si no encuentra devuelve una lista vacia
    @RequestMapping(value = "/colab/name/{colabname}", method = RequestMethod.GET)
    public ResponseEntity<List<ColaboradorResource>> getColabListByName(@PathVariable String colabname) {

        List<ColaboradorResource> colabList = service.getColabListByName(colabname);
        return colabList != null ? ResponseEntity.ok(colabList) : ResponseEntity.notFound().build();
    }

    // busca a colaboradores cuyo type contenga el string "type" y devuelve una lista
    // con los datos de colaborador y su foto de perfil
    // si no encuentra devuelve una lista vacia
    @RequestMapping(value = "/colab/type/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ColaboradorResource>> getColabListByType(@PathVariable String type) {

        List<ColaboradorResource> colabList = service.getColabListByType(type);
        return colabList != null ? ResponseEntity.ok(colabList) : ResponseEntity.notFound().build();
    }

    // BUSQUEDA DE PUBLICACIONES
    //
    // busca las publicaciones que contengan un hashtag coincidente con "tag"y devuelve
    // una lista con los datos de las publicaciones (menos los ids) y el nombre del usuario que hizo
    // la publicacion
    // si no encuentra devuelve una lista vacia
    @RequestMapping(value = "/publi/{tag}", method = RequestMethod.GET)
    public ResponseEntity<List<PreviewPublicacion>> getPubliListByTag(@PathVariable String tag) {

        List<PreviewPublicacion> publiList = service.getPubliListByTag("#" + tag);
        return publiList != null ? ResponseEntity.ok(publiList) : ResponseEntity.notFound().build();
    }
}
