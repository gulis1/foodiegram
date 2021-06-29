package main.domain.converter;

import main.domain.resource.ValoracionResource;
import main.persistence.entity.Valoracion;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValoracionConverter implements Converter<Optional<Valoracion>, ValoracionResource> {

    @Override
    public ValoracionResource convert(Optional<Valoracion> source){

        if (!source.isPresent())
            return null;

        ValoracionResource respose = new ValoracionResource();
        respose.setPunt(source.get().getPunt());
        respose.setIdpubli(source.get().getIdpubli());
        respose.setIduser(source.get().getIduser());
        return respose;
    }
}
