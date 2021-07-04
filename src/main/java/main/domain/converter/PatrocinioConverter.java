package main.domain.converter;


import main.domain.resource.PatrocinioResource;
import main.persistence.entity.Sponsor;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class PatrocinioConverter implements Converter<Optional<Sponsor>, PatrocinioResource> {

    @Override
    public PatrocinioResource convert(Optional<Sponsor> source) {
        return source.map(sponsor -> new PatrocinioResource(sponsor.getSponsorid(), sponsor.getEndtime())).orElse(null);
    }
}
