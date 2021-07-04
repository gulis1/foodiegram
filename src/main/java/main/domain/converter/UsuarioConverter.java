package main.domain.converter;

import main.domain.resource.UsuarioResource;
import main.persistence.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioConverter implements Converter<Optional<User>, UsuarioResource> {

    @Override
    public UsuarioResource convert(Optional<User> source ){

        if (!source.isPresent())
            return null;

        UsuarioResource response = new UsuarioResource();
        response.setId(source.get().getUserid());
        response.setName(source.get().getName());
        response.setEmail(source.get().getEmail());
        response.setImage(source.get().getImage());
        return response;
    }
}
