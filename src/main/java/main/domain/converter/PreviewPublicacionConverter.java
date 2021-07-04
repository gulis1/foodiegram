package main.domain.converter;

import main.domain.resource.PreviewPublicacion;
import main.persistence.entity.Post;
import org.springframework.core.convert.converter.Converter;

import java.text.DecimalFormat;
import java.util.Optional;


public class PreviewPublicacionConverter implements Converter<Optional<Post>, PreviewPublicacion> {

    @Override
    public PreviewPublicacion convert(Optional<Post> source) {

        DecimalFormat df = new DecimalFormat("#.##");
        return source.map(publicacion -> new PreviewPublicacion(publicacion.getPostid(), publicacion.getImage(), df.format(publicacion.getAvg()), publicacion.getText())).orElse(null);
    }
}
