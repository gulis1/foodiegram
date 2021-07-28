package main.application.service;

import main.domain.converter.PreviewPublicacionConverter;
import main.domain.converter.PreviewUserConverter;
import main.domain.converter.RestaurantConverter;
import main.domain.resource.PostPreview;
import main.domain.resource.RestaurantResource;
import main.domain.resource.UserPreview;
import main.persistence.entity.Post;
import main.persistence.entity.Restaurant;
import main.persistence.entity.User;
import main.persistence.repository.PostRepo;
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final PreviewUserConverter converterPreviewUser = new PreviewUserConverter();
    private final PreviewPublicacionConverter converterPreviewPubli = new PreviewPublicacionConverter();
    private final RestaurantConverter collabConverter = new RestaurantConverter();

    @Autowired
    private UserRepo repoUser;

    @Autowired
    private RestaurantRepo collabRepo;

    @Autowired
    private PostRepo postRepo;

    // BUSQUEDA DE USUARIOS
    //
    // devuelve una lista de usuarios cuyo nombre contenga username
    @Override
    public List<UserPreview> getUserList(String username) {

        List<User> userList = repoUser.findBynameContainingIgnoreCase(username);
        return userList.stream().map(x -> converterPreviewUser.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // devuelve una lista de usuarios por numero de publicaciones
    public List<UserPreview> getUserListByPubli() {

        List<User> userList = repoUser.findByPopuPubli();
        return userList.stream().map(x -> converterPreviewUser.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // devuelve una lista de usuarios por numero de valoraciones recibidas
    public List<UserPreview> getUserListByVal() {

        List<User> userList = repoUser.findByPopuVal();
        return userList.stream().map(x -> converterPreviewUser.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // BUSQUEDA DE LOCALES
    //
    // devuelve una lista de colaboradores cuyo nombre contenga colabname
    @Override
    public List<RestaurantResource> getColabListByName(String colabname) {

        List<Restaurant> collabs = collabRepo.findByUsername(colabname);
        return collabs.stream().map(x -> collabConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // devuelve una lista de colaboradores cuyo type contenga type
    @Override
    public List<RestaurantResource> getColabListByType(String type) {

        List<Restaurant> colabJuser = collabRepo.findByType(type);
        return colabJuser.stream().map(x -> collabConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // BUSQUEDA DE PUBLICACIONES
    //
    // devuelve una lista de publicaciones cuyo texto contenga un hashtag coincidente con tag
    @Override
    public List<PostPreview> getPubliListByTag(String tag) {

        List<Post> publiJuser = postRepo.findByTag(tag);
        return publiJuser.stream().map(x -> converterPreviewPubli.convert(Optional.of(x))).collect(Collectors.toList());
    }
}
