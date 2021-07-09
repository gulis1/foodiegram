package main.domain.converter;

import main.domain.resource.RatingResource;
import main.persistence.entity.Rating;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValoracionConverter implements Converter<Optional<Rating>, RatingResource> {

    @Override
    public RatingResource convert(Optional<Rating> source){

        if (!source.isPresent())
            return null;

        RatingResource respose = new RatingResource();
        respose.setPunt(source.get().getScore());
        respose.setIdpubli(source.get().getPost());
        respose.setIduser(source.get().getUser());
        return respose;
    }
}
