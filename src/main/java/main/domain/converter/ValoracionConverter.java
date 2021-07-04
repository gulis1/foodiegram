package main.domain.converter;

import main.domain.resource.ValoracionResource;
import main.persistence.entity.Rating;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValoracionConverter implements Converter<Optional<Rating>, ValoracionResource> {

    @Override
    public ValoracionResource convert(Optional<Rating> source){

        if (!source.isPresent())
            return null;

        ValoracionResource respose = new ValoracionResource();
        respose.setPunt(source.get().getScore());
        respose.setIdpubli(source.get().getPost());
        respose.setIduser(source.get().getUser());
        return respose;
    }
}
