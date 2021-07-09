package main.domain.converter;


import main.domain.resource.SponsorResource;
import main.persistence.entity.Sponsor;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class PatrocinioConverter implements Converter<Optional<Sponsor>, SponsorResource> {

    @Override
    public SponsorResource convert(Optional<Sponsor> source) {
        return source.map(sponsor -> new SponsorResource(sponsor.getSponsorid(), sponsor.getEndtime())).orElse(null);
    }
}
