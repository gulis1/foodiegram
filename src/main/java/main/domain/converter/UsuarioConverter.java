package main.domain.converter;

import main.domain.resource.UserResource;
import main.persistence.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioConverter implements Converter<Optional<User>, UserResource> {

    @Override
    public UserResource convert(Optional<User> source ){

        if (!source.isPresent())
            return null;

        UserResource response = new UserResource();
        response.setId(source.get().getUserid());
        response.setName(source.get().getName());
        response.setEmail(source.get().getEmail());
        response.setImage(source.get().getImage());
        return response;
    }
}
