package main.domain.converter;

import main.domain.resource.UserPreview;
import main.persistence.entity.User;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class PreviewUserConverter implements Converter<Optional<User>, UserPreview> {

    @Override
    public UserPreview convert(Optional<User> source) {
        return source.map(usuario -> new UserPreview(usuario.getName(), usuario.getImage())).orElse(null);
    }
}
