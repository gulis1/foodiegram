package main.application.service;

import main.domain.converter.ColaboradorConverter;
import main.domain.converter.PreviewPublicacionConverter;
import main.domain.converter.PreviewUserConverter;
import main.domain.resource.ColaboradorResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PreviewUsuario;
import main.persistence.entity.Restaurant;
import main.persistence.entity.Post;
import main.persistence.entity.User;

import main.persistence.repository.PostRepo;
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final PreviewUserConverter converterPreviewUser = new PreviewUserConverter();
    private final PreviewPublicacionConverter converterPreviewPubli = new PreviewPublicacionConverter();
    private final ColaboradorConverter collabConverter = new ColaboradorConverter();

    @Autowired
    private RepoUsuario repoUser;

    @Autowired
    private RestaurantRepo collabRepo;

    @Autowired
    private PostRepo postRepo;

    // BUSQUEDA DE USUARIOS
    //
    // devuelve una lista de usuarios cuyo nombre contenga username
    @Override
    public List<PreviewUsuario> getUserList(String username) {

        List<User> userList = repoUser.findBynameContainingIgnoreCase(username);
        return userList.stream().map(x -> converterPreviewUser.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // devuelve una lista de usuarios por numero de publicaciones
    public List<PreviewUsuario> getUserListByPubli() {

        List<User> userList = repoUser.findByPopuPubli();
        return userList.stream().map(x -> converterPreviewUser.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // devuelve una lista de usuarios por numero de valoraciones recibidas
    public List<PreviewUsuario> getUserListByVal() {

        List<User> userList = repoUser.findByPopuVal();
        return userList.stream().map(x -> converterPreviewUser.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // BUSQUEDA DE LOCALES
    //
    // devuelve una lista de colaboradores cuyo nombre contenga colabname
    @Override
    public List<ColaboradorResource> getColabListByName(String colabname) {

        List<Restaurant> collabs = collabRepo.findByUsername(colabname);
        return collabs.stream().map(x -> collabConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // devuelve una lista de colaboradores cuyo type contenga type
    @Override
    public List<ColaboradorResource> getColabListByType(String type) {

        List<Restaurant> colabJuser = collabRepo.findByType(type);
        return colabJuser.stream().map(x -> collabConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // BUSQUEDA DE PUBLICACIONES
    //
    // devuelve una lista de publicaciones cuyo texto contenga un hashtag coincidente con tag
    @Override
    public List<PreviewPublicacion> getPubliListByTag(String tag) {

        List<Post> publiJuser = postRepo.findByTag(tag);
        return publiJuser.stream().map(x -> converterPreviewPubli.convert(Optional.of(x))).collect(Collectors.toList());
    }
}
