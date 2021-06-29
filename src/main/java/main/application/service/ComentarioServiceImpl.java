package main.application.service;

import main.domain.converter.ComentarioConverter;
import main.domain.resource.ComentarioResource;
import main.persistence.entity.Comentario;
import main.persistence.repository.RepoComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.Optional;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    RepoComentario repoComen;

    private ComentarioConverter comentarioConverter = new ComentarioConverter();


    @Override
    public ComentarioResource editComentario(Integer comID, String text) throws IllegalArgumentException, NoPermissionException {


        Optional<Comentario> comment = repoComen.findById(comID);

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
    public ComentarioResource deleteComentario(Integer comID) throws NoPermissionException {

        Optional<Comentario> comment = repoComen.findById(comID);

        comment.ifPresent(comentario -> repoComen.delete(comentario));

        return comentarioConverter.convert(comment);


    }

}
