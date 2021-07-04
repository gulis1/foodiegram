package main.domain.converter;

import main.domain.resource.ColaboradorResource;
import main.persistence.entity.Restaurant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ColaboradorConverter implements Converter<Optional<Restaurant>,ColaboradorResource> {

    @Override
    public ColaboradorResource convert(Optional<Restaurant> source) {


        if(!source.isPresent())
            return null;

        ColaboradorResource response= new ColaboradorResource();
        response.setIdUser(source.get().getRestaurantid());
        response.setType(source.get().getType());
        response.setPais(source.get().getCountry());
        response.setCiudad(source.get().getCity());
        response.setCalle(source.get().getStreet());
        response.setVip(source.get().getVip());

        return response;
    }

}
