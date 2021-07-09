package main.application.service;

import main.domain.converter.ComentarioConverter;
import main.domain.resource.CommentResource;
import main.persistence.entity.Comment;
import main.persistence.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.Optional;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    CommentRepo repoComen;

    private ComentarioConverter comentarioConverter = new ComentarioConverter();


    @Override
    public CommentResource editComentario(Integer comID, String text) throws IllegalArgumentException, NoPermissionException {


        Optional<Comment> comment = repoComen.findById(comID);

        if (!comment.isPresent())
            return null;

        else {

            if (text != null)
                comment.get().setText(text);
            else
                throw new IllegalArgumentException();

        }

            repoComen.save(comment.get());

        return  comentarioConverter.convert(comment);
    }

    @Override
    public CommentResource deleteComentario(Integer comID) throws NoPermissionException {

        Optional<Comment> comment = repoComen.findById(comID);

        comment.ifPresent(comentario -> repoComen.delete(comentario));

        return comentarioConverter.convert(comment);


    }

}
