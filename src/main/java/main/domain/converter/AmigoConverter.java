package main.domain.converter;

import main.domain.resource.AmigoResource;
import main.persistence.entity.Amigo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AmigoConverter implements Converter<Optional<Amigo>, AmigoResource> {

    @Override
    public AmigoResource convert(Optional<Amigo> amigo) {

        if(!amigo.isPresent())
            return null;

        AmigoResource response = new AmigoResource();
        response.setIduser1(amigo.get().getIduser1());
        response.setIduser2(amigo.get().getIduser2());
        return response;
    }
}
