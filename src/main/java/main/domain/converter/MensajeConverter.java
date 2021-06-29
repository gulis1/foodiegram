package main.domain.converter;

import main.domain.resource.MensajeResource;
import main.persistence.entity.Mensaje;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MensajeConverter implements Converter<Optional<Mensaje>, MensajeResource> {

    @Override
    public  MensajeResource convert(Optional<Mensaje> source){

        if (!source.isPresent())
            return null;

        MensajeResource response = new MensajeResource();
        response.setId(source.get().getId());
        response.setIduser1(source.get().getIduser1());
        response.setIduser2(source.get().getIduser2());
        response.setText(source.get().getText());
        return response;
    }


}