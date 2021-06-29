package main.domain.converter;


import main.domain.resource.PatrocinioResource;
import main.persistence.entity.Patrocinio;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class PatrocinioConverter implements Converter<Optional<Patrocinio>, PatrocinioResource> {

    @Override
    public PatrocinioResource convert(Optional<Patrocinio> source) {
        return source.map(patrocinio -> new PatrocinioResource(patrocinio.getId(), patrocinio.getEndtime())).orElse(null);
    }
}
