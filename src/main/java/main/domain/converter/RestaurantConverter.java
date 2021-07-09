package main.domain.converter;

import main.domain.resource.FollowResource;
import main.domain.resource.RestaurantResource;
import main.persistence.entity.Restaurant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestaurantConverter implements Converter<Optional<Restaurant>, RestaurantResource> {

    @Override
    public RestaurantResource convert(Optional<Restaurant> source) {


        if(!source.isPresent())
            return null;

        RestaurantResource response= new RestaurantResource();
        response.setIdUser(source.get().getRestaurantid());
        response.setType(source.get().getType());
        response.setPais(source.get().getCountry());
        response.setCiudad(source.get().getCity());
        response.setCalle(source.get().getStreet());
        response.setVip(source.get().getVip());

        return response;
    }

}
