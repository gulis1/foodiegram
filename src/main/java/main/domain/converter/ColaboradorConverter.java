package main.domain.converter;

import main.domain.resource.ColaboradorResource;
import main.persistence.entity.Colaborador;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ColaboradorConverter implements Converter<Optional<Colaborador>,ColaboradorResource> {

    @Override
    public ColaboradorResource convert(Optional<Colaborador> source) {


        if(!source.isPresent())
            return null;

        ColaboradorResource response= new ColaboradorResource();
        response.setIdUser(source.get().getId());
        response.setOrigin(source.get().getOrigin());
        response.setType(source.get().getType());
        response.setPais(source.get().getPais());
        response.setCiudad(source.get().getCiudad());
        response.setCalle(source.get().getCalle());
        response.setVip(source.get().getVip());

        return response;
    }

}
