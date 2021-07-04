package main.domain.converter;

import main.domain.resource.AmigoResource;
import main.persistence.entity.Follow;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AmigoConverter implements Converter<Optional<Follow>, AmigoResource> {

    @Override
    public AmigoResource convert(Optional<Follow> amigo) {

        if(!amigo.isPresent())
            return null;

        AmigoResource response = new AmigoResource();
        response.setIduser1(amigo.get().getFollower());
        response.setIduser2(amigo.get().getFollowed());
        return response;
    }
}
