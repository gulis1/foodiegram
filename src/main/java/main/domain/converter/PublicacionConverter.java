package main.domain.converter;

import main.domain.resource.PublicacionResource;
import main.persistence.entity.Publicacion;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Optional;

@Component
public class PublicacionConverter implements Converter<Optional<Publicacion>, PublicacionResource> {

    @Override
    public PublicacionResource convert(Optional<Publicacion> source){

        DecimalFormat df = new DecimalFormat("#.##");
        if (!source.isPresent())
            return null;

        PublicacionResource response = new PublicacionResource();
        response.setId(source.get().getId());
        response.setIduser(source.get().getIduser());
        response.setText(source.get().getText());
        response.setImage(source.get().getImage());
        response.setCiudad(source.get().getCiudad());
        response.setPais(source.get().getPais());
        response.setFecha(source.get().getFecha());
        response.setMedia(df.format(source.get().getMedia()));
        response.setNumerototalval(source.get().getNumerototalval());

        return response;
    }


}
