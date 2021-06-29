package main.domain.converter;


import main.domain.resource.ComentarioResource;
import main.persistence.entity.Comentario;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ComentarioConverter implements Converter<Optional<Comentario>, ComentarioResource> {

    @Override
    public ComentarioResource convert(Optional<Comentario> source){

        if (!source.isPresent())
            return null;

        ComentarioResource response=new ComentarioResource();
        response.setId(source.get().getId());
        response.setIdpubli(source.get().getIdpubli());
        response.setIduser(source.get().getAutor().getId());
        response.setText(source.get().getText());
        response.setPfp(source.get().getAutor().getImage());
        response.setUser(source.get().getAutor().getName());
        return response;

    }

}
