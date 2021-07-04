package main.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import main.domain.converter.MensajeConverter;
import main.domain.resource.MensajeResource;

import main.persistence.entity.Mensaje;
import main.persistence.entity.Usuario;
import main.persistence.repository.MessageRepo;
import main.persistence.repository.UserRepo;

import main.rest.forms.MessageForm;

import org.junit.jupiter.api.Test;;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)

class MensajeServiceImplTest {

    /**
     * TEST GET MENSAJE
     */

    @Mock
    MensajeConverter MensConvMock;
    RepoMensaje repoMensMock;
    MensajeResource resoMensMock;
    Integer mensIDMock;

    @Test
    void getMensajeOK() {

        //DEVUELVE MensajeResource
        when(MensConvMock.convert((repoMensMock).findOne(mensIDMock))).thenReturn(resoMensMock);
        assertNotNull(resoMensMock);
    }

    @Test
    void getMensajeNull(){

        //DEVUELVE NULL
        resoMensMock = null;
        when(MensConvMock.convert((repoMensMock).findOne(mensIDMock))).thenReturn(resoMensMock);
        assertNull(resoMensMock);
    }

    /**
     * TEST DELETE MENSAJE
     */

    @Mock
    Mensaje mensMock;

    @Test
    void deleteMensajeOK() {

        when(repoMensMock.findOne(mensIDMock)).thenReturn(mensMock);
        when(mensMock != null).thenReturn(false);

        assertNotNull(resoMensMock);

    }

    @Test
    void deleteMensajeNull(){

        //DEVUELVE NULL
        mensMock=null;
        when(mensMock != null).thenReturn(true);
        when(repoMensMock.findOne(mensIDMock)).thenReturn(mensMock);
        when(MensConvMock.convert(mensMock)).thenReturn(resoMensMock);
        assertNull(resoMensMock);
    }

    /**
     * TEST SET MENSAJE
     */

    @Mock
    RepoUsuario repoUsuarioMock;
    Usuario usuarioMock;
    Usuario usuarioMock2;
    MessageForm mensajeMock;

    @Test
    void setMensajeOK() {

        //DEVUELVE Mensajeresource
        when(repoUsuarioMock.findByName(mensajeMock.getReceiver())).thenReturn(usuarioMock);
        when(repoUsuarioMock.findByName(mensajeMock.getReceiver())).thenReturn(usuarioMock2);
        when(usuarioMock == null || usuarioMock2 == null).thenReturn(false);
        when(new Mensaje(usuarioMock.getId(),usuarioMock2.getId(),mensajeMock.getText())).thenReturn(mensMock);
        when(repoMensMock.save(mensMock)).thenReturn(null);
        when(MensConvMock.convert(mensMock));
        assertNotNull(mensMock);

    }

    @Test
    void setMensajeNull(){

        //DEVUELVE NULL
        usuarioMock2 = null;
        when(repoUsuarioMock.findByName(mensajeMock.getReceiver())).thenReturn(usuarioMock);
        when(repoUsuarioMock.findByName(mensajeMock.getReceiver())).thenReturn(usuarioMock2);
        when(usuarioMock == null || usuarioMock2 == null).thenThrow(new IllegalArgumentException("Both users should exist."));
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> usuarioMock2.getId());
        assertNotNull(exception.getMessage());

    }
}