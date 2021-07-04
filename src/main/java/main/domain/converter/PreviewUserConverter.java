package main.domain.converter;

import main.domain.resource.PreviewUsuario;
import main.persistence.entity.User;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class PreviewUserConverter implements Converter<Optional<User>, PreviewUsuario> {

    @Override
    public PreviewUsuario convert(Optional<User> source) {
        return source.map(usuario -> new PreviewUsuario(usuario.getName(), usuario.getImage())).orElse(null);
    }
}
