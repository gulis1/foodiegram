package main.domain.converter;

import main.domain.resource.PublicacionResource;
import main.persistence.entity.Post;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Optional;

@Component
public class PublicacionConverter implements Converter<Optional<Post>, PublicacionResource> {

    @Override
    public PublicacionResource convert(Optional<Post> source){

        DecimalFormat df = new DecimalFormat("#.##");
        if (!source.isPresent())
            return null;

        PublicacionResource response = new PublicacionResource();
        response.setId(source.get().getPostid());
        response.setIduser(source.get().getUser().getUserid());
        response.setText(source.get().getText());
        response.setImage(source.get().getImage());
        response.setCiudad(source.get().getCity());
        response.setPais(source.get().getCountry());
        response.setFecha(source.get().getDay());
        response.setMedia(df.format(source.get().getAvg()));
        response.setNumerototalval(source.get().getNumratings());

        return response;
    }


}
