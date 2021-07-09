package main.domain.converter;

import main.domain.resource.PostPreview;
import main.persistence.entity.Post;
import org.springframework.core.convert.converter.Converter;

import java.text.DecimalFormat;
import java.util.Optional;


public class PreviewPublicacionConverter implements Converter<Optional<Post>, PostPreview> {

    @Override
    public PostPreview convert(Optional<Post> source) {

        DecimalFormat df = new DecimalFormat("#.##");
        return source.map(publicacion -> new PostPreview(publicacion.getPostid(), publicacion.getImage(), df.format(publicacion.getAvg()), publicacion.getText())).orElse(null);
    }
}
