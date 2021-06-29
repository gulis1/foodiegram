package main.domain.converter;

import main.domain.resource.MeetupResource;
import main.persistence.entity.MeetUp;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MeetUpConverter implements Converter<Optional<MeetUp>, MeetupResource> {


    @Override
    public MeetupResource convert(Optional<MeetUp> source) {


        if(!source.isPresent())
            return null;

        MeetupResource response= new MeetupResource();

        response.setEventId(source.get().getId());
        response.setEventId(source.get().getIduser());
        return response;
    }
}
