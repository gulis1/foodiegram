package main.domain.converter;

import main.domain.resource.PreviewPublicacion;
import main.persistence.entity.Publicacion;
import org.springframework.core.convert.converter.Converter;

import java.text.DecimalFormat;
import java.util.Optional;


public class PreviewPublicacionConverter implements Converter<Optional<Publicacion>, PreviewPublicacion> {

    @Override
    public PreviewPublicacion convert(Optional<Publicacion> source) {

        DecimalFormat df = new DecimalFormat("#.##");
        return source.map(publicacion -> new PreviewPublicacion(publicacion.getId(), publicacion.getImage(), df.format(publicacion.getMedia()), publicacion.getText())).orElse(null);
    }
}
