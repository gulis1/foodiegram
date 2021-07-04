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
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.PostRepo;
import main.persistence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscoverServiceImpl implements DiscoverService {

    @Autowired
    private PostRepo repoPublicacion;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestaurantRepo repoColab;

    private PreviewUserConverter usuarioConverter = new PreviewUserConverter();
    private PreviewPublicacionConverter publicacionConverter = new PreviewPublicacionConverter();
    private ColaboradorConverter collabConverter = new ColaboradorConverter();


    public List<PreviewPublicacion> discoverByAmigo(){

        Integer userid=Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Post> publi = repoPublicacion.fromFriends(userid);

        return publi.stream().map(x -> publicacionConverter.convert(Optional.of(x))).collect(Collectors.toList());

    }


    public List<PreviewPublicacion> discoverBestRated(String period, String country, String city) throws IllegalArgumentException{

        List<Post> publi = repoPublicacion.bestRated(getDayAmount(period), country, city);

        return publi.stream().map(x -> publicacionConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<PreviewPublicacion> discoverMostRated(String period, String country, String city) {

        List<Post> publi = repoPublicacion.mostRated(getDayAmount(period),  country, city);

        return publi.stream().map(x -> publicacionConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<PreviewUsuario> findFollowedByFriends(Integer userid) {
        List<User> list = userRepo.findFollowedByFriends(userid);

        return list.stream().map(x -> usuarioConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<PreviewUsuario> userWhoFollowXAlsoFollowY(String userName) {

        Optional<User> user = userRepo.findByName(userName);

        if (!user.isPresent())
            return null;

        List<User> list = userRepo.usersFollowedByUsersWhoFollow(user.get().getUserid());

        return list.stream().map(x -> usuarioConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<ColaboradorResource> findCollabs(String country, String city) {

        List<Restaurant> collabs = repoColab.descubrirCollab(country,city);

        return collabs.stream().map(x -> collabConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }


    private Integer getDayAmount(String periodo) {

        switch (periodo) {
            case "day": return 1;
            case "week": return 7;
            case "month": return 30;
            case "year": return 365;
            case "allTime": return -1;
            default: throw new IllegalArgumentException("Invalid period");
        }


    }


}
