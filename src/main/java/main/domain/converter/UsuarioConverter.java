package main.domain.converter;

import main.domain.resource.UsuarioResource;
import main.persistence.entity.Usuario;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioConverter implements Converter<Optional<Usuario>, UsuarioResource> {

    @Override
    public UsuarioResource convert(Optional<Usuario> source ){

        if (!source.isPresent())
            return null;

        UsuarioResource response = new UsuarioResource();
        response.setId(source.get().getId());
        response.setName(source.get().getName());
        response.setEmail(source.get().getEmail());
        response.setImage(source.get().getImage());
        return response;
    }
}
