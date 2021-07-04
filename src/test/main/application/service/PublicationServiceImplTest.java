package main.application.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import main.domain.converter.ComentarioConverter;
import main.domain.converter.PublicacionConverter;
import main.domain.converter.ValoracionConverter;
import main.domain.resource.ComentarioResource;
import main.domain.resource.PublicacionResource;
import main.domain.resource.ValoracionResource;

import main.persistence.IDs.IDvaloracion;
import main.persistence.entity.Comentario;
import main.persistence.entity.Publicacion;
import main.persistence.entity.Usuario;
import main.persistence.entity.Valoracion;
import main.persistence.repository.CommentRepo;
import main.persistence.repository.PostRepo;
import main.persistence.repository.UserRepo;
import main.persistence.repository.RatingRepo;

import main.rest.forms.CommentForm;
import main.rest.forms.RatingForm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.lang.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)

class PublicationServiceImplTest {

    /**
     * TEST GET POST
     */

    @Mock
    PublicacionResource getPostMock;
    PublicacionConverter converterPubliMock;
    RepoPublicacion repoPubliMock;

    @Test
    void getPost() {

        when(converterPubliMock.convert(repoPubliMock.findOne(117))).thenReturn(getPostMock);
        assertNotNull(getPostMock);
    }

    /**
     * TEST EDIT POST
     */

    @Mock
    PublicacionResource editPostMock;
    Publicacion publiMock;

    @Test
    void editPostOk() {

        String text = "This belongs in a museum";
        String loc = "Piltover";

        when(text == null || loc == null).thenReturn(false);
        when(repoPubliMock.findOne(117)).thenReturn(publiMock);
        when(publiMock != null).thenReturn(true);
        when(text != null).thenReturn(true);
        when(loc != null).thenReturn(true);
        when(converterPubliMock.convert(publiMock)).thenReturn(editPostMock);
        assertNotNull(editPostMock);
    }

    @Test
    void editPostException(){

        String textN = "This belongs in a museum";
        String locN = null;
        when(textN == null || locN == null).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> { });

    }

    /**
     * TEST DELETE POST
     */

    @Mock
    PublicacionResource deletePostMock;

    @Test
    void deletePost() {

        when(repoPubliMock.findOne(117)).thenReturn(publiMock);
        when(publiMock != null).thenReturn(true);
        when(converterPubliMock.convert(publiMock)).thenReturn(deletePostMock);
        assertNotNull(deletePostMock);
    }

    @Test
    void deletePostNull(){

        when(repoPubliMock.findOne(117)).thenReturn(publiMock);
        when(publiMock != null).thenReturn(false);
        when(converterPubliMock.convert(publiMock)).thenReturn(deletePostMock);
        assertNull(deletePostMock);
    }

    /**
     * TEST GET RATINGS
     */

    @Mock
    List<ValoracionResource> getRatingsMock;
    List<Valoracion> valoracionesMock;
    RepoValoracion repoValoracionMock;
    ValoracionConverter ValoracionConverterMock;

    @Test
    void getRatingsOk() {

        when(repoPubliMock.findOne(117)).thenReturn(publiMock);
        when(publiMock == null).thenReturn(false);
        when(repoValoracionMock.findByIdpubli(117)).thenReturn(valoracionesMock);
        when(valoracionesMock.stream().map(ValoracionConverterMock::convert).collect(Collectors.toList())).thenReturn(getRatingsMock);
        assertNotNull(getRatingsMock);
    }

    @Test
    void getRatingNull(){

        when(repoPubliMock.findOne(117)).thenReturn(publiMock);
        when(publiMock == null).thenReturn(true);
        assertNull(getRatingsMock);
    }

    /**
     * TEST SET RATINGS
     */

    @Mock
    ValoracionResource valoracionResourceMock;
    RatingForm formMock;
    Valoracion valMock;

    @Test
    void setRatingOk() {

        when(new Valoracion(formMock.getPubID(), formMock.getUserID(), formMock.getScore())).thenReturn(valMock);
        when(ValoracionConverterMock.convert(valMock)).thenReturn(valoracionResourceMock);
        assertNotNull(valoracionResourceMock);
    }

    @Test
    void setRatingException(){

        when(formMock.getScore() < 0 || formMock.getScore() > 5).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> { });

    }

    /**
     * TEST GET RATINGS VALORATION
     */

    @Mock
    RepoUsuario repoUsuarioMock;
    Usuario usuarioMock;

    @Test
    void getRatingValOk() {

        String user = "Grogu";
        Integer id = 1256;
        when(repoUsuarioMock.findByName(user)).thenReturn(usuarioMock);
        when(usuarioMock == null).thenReturn(false);
        when(ValoracionConverterMock.convert(repoValoracionMock.findOne(new IDvaloracion(id, usuarioMock.getId())))).thenReturn(valoracionResourceMock);
        assertNotNull(valoracionResourceMock);
    }

    @Test
    void getRatingValNull() {

        String user = null;
        Integer id = 1256;
        when(repoUsuarioMock.findByName(user)).thenReturn(usuarioMock);
        when(usuarioMock == null).thenReturn(true);
        assertNull(valoracionResourceMock);

    }

    /**
     * TEST DELETE RATINGS VALORATION
     */

    @Test
    void deleteRatingValOk() {

        when(repoValoracionMock.findOne(new IDvaloracion(formMock.getPubID(), formMock.getUserID()))).thenReturn(valMock);
        when(valMock != null).thenReturn(true);
        when(ValoracionConverterMock.convert(valMock)).thenReturn(valoracionResourceMock);
        assertNotNull(valoracionResourceMock);
    }

    @Test
    void deleteRatingValNull(){

        when(repoValoracionMock.findOne(new IDvaloracion(formMock.getPubID(), formMock.getUserID()))).thenReturn(valMock);
        when(valMock != null).thenReturn(false);
        when(ValoracionConverterMock.convert(valMock)).thenReturn(valoracionResourceMock);
        assertNull(valoracionResourceMock);

    }

    /**
     * TEST GET COMMENTS
     */

    @Mock
    List<ComentarioResource> comentarioResourcesMock;
    List<Comentario> comentariosMock;
    RepoComentario repoComenMock;
    ComentarioConverter converterComMock;

    @Test
    void getCommentsOk() {

        Integer pubID = 123;
        when(repoPubliMock.findOne(pubID)).thenReturn(publiMock);
        when(publiMock == null).thenReturn(false);
        when(repoComenMock.findByIdpubliOrderByIdAsc(pubID)).thenReturn(comentariosMock);
        when(comentariosMock.stream().map(converterComMock::convert).collect(Collectors.toList())).thenReturn(comentarioResourcesMock);
        assertNotNull(comentarioResourcesMock);


    }

    @Test
    void getCommentsNull(){

        Integer pubID = 123;
        when(repoPubliMock.findOne(pubID)).thenReturn(publiMock);
        when(publiMock == null).thenReturn(true);
        assertNull(comentarioResourcesMock);

    }

    /**
     * TEST GET COMMENTS
     */

    @Mock
    ComentarioResource ComentarioResourceMock;
    Comentario comentarioMock;
    CommentForm commentFormMock;

    @Test
    void setCommentOk() {

        when(commentFormMock.getText() == null || commentFormMock.getText().length() == 0).thenReturn(false);
        when(new Comentario(commentFormMock.getPubID(), new Usuario(commentFormMock.getUserID()), commentFormMock.getText())).thenReturn(comentarioMock);
        when(converterComMock.convert(comentarioMock)).thenReturn(ComentarioResourceMock);
        assertNotNull(ComentarioResourceMock);
    }

    @Test
    void setCommentNull(){

        when(commentFormMock.getText() == null || commentFormMock.getText().length() == 0).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
        });

    }


}