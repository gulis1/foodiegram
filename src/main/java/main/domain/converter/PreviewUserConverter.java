package main.domain.converter;

import main.domain.resource.PreviewUsuario;
import main.persistence.entity.Usuario;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class PreviewUserConverter implements Converter<Optional<Usuario>, PreviewUsuario> {

    @Override
    public PreviewUsuario convert(Optional<Usuario> source) {
        return source.map(usuario -> new PreviewUsuario(usuario.getName(), usuario.getImage())).orElse(null);
    }
}
