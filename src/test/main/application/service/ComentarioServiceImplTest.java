package main.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import main.domain.converter.ComentarioConverter;
import main.domain.resource.ComentarioResource;

import main.persistence.entity.Comentario;
import main.persistence.repository.CommentRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)

class ComentarioServiceImplTest {

    /**
     * TEST EDIT COMENTARIO
     */

    @Mock
    RepoComentario repoCommentMock;
    ComentarioConverter commentConverterMock;
    ComentarioResource comentarioResoMock;

    @Test
    void editComentario() {

        Comentario commentMock = Mockito.mock(Comentario.class);
        Integer comIdMock = Mockito.mock(Integer.class);
        String textMock = Mockito.mock(String.class);

        when(repoCommentMock.findOne(comIdMock)).thenReturn(commentMock);
        when(commentMock == null).thenReturn(false);
        when(textMock != null).thenReturn( true);

        //Devuelve un ComentarioResource  con el mismo comID  y texto modificado
        when(commentConverterMock.convert(commentMock)).thenReturn(comentarioResoMock);
        assertNotNull(comentarioResoMock);

    }

    @Test
    void editComentarioTestException(){

        Comentario commentMocki = null;
        Integer comIdMock = Mockito.mock(Integer.class);
        String textMock = Mockito.mock(String.class);

        when(repoCommentMock.findOne(comIdMock)).thenReturn(commentMocki);
        when(commentMocki == null).thenReturn(false);
        when(textMock != null).thenThrow(new IllegalArgumentException());
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> commentMocki.getText());
        assertNotNull(exception.getMessage());

    }


    /**
     * TEST DELETE COMENTARIO
     */

    @Test
    void deleteComentario() {

        Comentario commentMocki = Mockito.mock(Comentario.class);
        Integer comIdMock = Mockito.mock(Integer.class);

        when(repoCommentMock.findOne(comIdMock)).thenReturn(commentMocki);
        when(commentMocki != null).thenReturn(true);
        when(commentConverterMock.convert(commentMocki)).thenReturn(comentarioResoMock);
        assertNotNull(comentarioResoMock);

    }

    @Test
    void deleteComentarioTestNull(){

        Comentario commentMocki = null;
        Integer comIdMock = Mockito.mock(Integer.class);
        comentarioResoMock = null;

        when(repoCommentMock.findOne(comIdMock)).thenReturn(commentMocki);
        when(commentMocki != null).thenReturn(false);
        when(commentConverterMock.convert(commentMocki)).thenReturn(comentarioResoMock);
        assertNull(comentarioResoMock);

    }
}