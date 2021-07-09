package main.domain.converter;


import main.domain.resource.CommentResource;
import main.persistence.entity.Comment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ComentarioConverter implements Converter<Optional<Comment>, CommentResource> {

    @Override
    public CommentResource convert(Optional<Comment> source){

        if (!source.isPresent())
            return null;

        CommentResource response=new CommentResource();
        response.setId(source.get().getCommentid());
        response.setIdpubli(source.get().getPost());
        response.setIduser(source.get().getAutor().getUserid());
        response.setText(source.get().getText());
        response.setPfp(source.get().getAutor().getImage());
        response.setUser(source.get().getAutor().getName());
        return response;

    }

}
