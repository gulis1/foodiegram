package main.domain.converter;

import main.domain.resource.FollowResource;
import main.persistence.entity.Follow;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FollowConverter implements Converter<Optional<Follow>, FollowResource> {

    @Override
    public FollowResource convert(Optional<Follow> amigo) {

        if(!amigo.isPresent())
            return null;

        FollowResource response = new FollowResource();
        response.setIduser1(amigo.get().getFollower());
        response.setIduser2(amigo.get().getFollowed());
        return response;
    }
}
