package main.application.service;


import main.domain.converter.ColaboradorConverter;
import main.domain.converter.PreviewPublicacionConverter;
import main.domain.converter.PreviewUserConverter;
import main.domain.resource.ColaboradorResource;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PreviewUsuario;
import main.persistence.entity.Colaborador;
import main.persistence.entity.Publicacion;
import main.persistence.entity.Usuario;
import main.persistence.repository.RepoColaborador;
import main.persistence.repository.RepoPublicacion;
import main.persistence.repository.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscoverServiceImpl implements DiscoverService {

    @Autowired
    private RepoPublicacion repoPublicacion;

    @Autowired
    private RepoUsuario repoUsuario;

    @Autowired
    private RepoColaborador repoColab;

    private PreviewUserConverter usuarioConverter = new PreviewUserConverter();
    private PreviewPublicacionConverter publicacionConverter = new PreviewPublicacionConverter();
    private ColaboradorConverter collabConverter = new ColaboradorConverter();


    public List<PreviewPublicacion> discoverByAmigo(){

        Integer userid=Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Publicacion> publi = repoPublicacion.fromFriends(userid);

        return publi.stream().map(x -> publicacionConverter.convert(Optional.of(x))).collect(Collectors.toList());

    }


    public List<PreviewPublicacion> discoverBestRated(String period, String country, String city) throws IllegalArgumentException{

        List<Publicacion> publi = repoPublicacion.bestRated(getDayAmount(period), country, city);

        return publi.stream().map(x -> publicacionConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<PreviewPublicacion> discoverMostRated(String period, String country, String city) {

        List<Publicacion> publi = repoPublicacion.mostRated(getDayAmount(period),  country, city);

        return publi.stream().map(x -> publicacionConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<PreviewUsuario> findFollowedByFriends(Integer userid) {
        List<Usuario> list = repoUsuario.findFollowedByFriends(userid);

        return list.stream().map(x -> usuarioConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<PreviewUsuario> userWhoFollowXAlsoFollowY(String userName) {

        Optional<Usuario> user = repoUsuario.findByName(userName);

        if (!user.isPresent())
            return null;

        List<Usuario> list = repoUsuario.usersFollowedByUsersWhoFollow(user.get().getId());

        return list.stream().map(x -> usuarioConverter.convert(Optional.of(x))).collect(Collectors.toList());
    }

    @Override
    public List<ColaboradorResource> findCollabs(String country, String city) {

        List<Colaborador> collabs = repoColab.descubrirCollab(country,city);

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
