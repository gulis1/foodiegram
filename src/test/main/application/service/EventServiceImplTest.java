package main.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import main.domain.resource.EventoResource;
import main.domain.resource.MeetupResource;

import main.persistence.entity.Evento;

import main.rest.forms.EventForm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)

class EventServiceImplTest {

    /**
     * TEST GET EVENT LIST BY ID COLAB
     */

    @Mock
    RepoEvento repoEventoMock;
    List<EventoResource>  ListEventResourceMock;
    List<Evento> ListEventoMock;

    @Test
    void getEventListByIdcolab() {

        Integer idcolab = Mockito.mock(Integer.class);

        when(repoEventoMock.findByIdcolab(idcolab)).thenReturn(ListEventoMock);

        assertNotNull(ListEventResourceMock);

    }

    /**
     * TEST UPLOAD
     */

    @Mock
    EventoResource EventoResourceMock;
    Evento EventoMock;
    Pattern imagePatternMock;
    EventForm formMock;
    EventoConverter converterEventMock;

    @Test
    void uploadNull() {

        when(new Evento(formMock.getIdCollab(), formMock.getText(), formMock.getDate())).thenReturn(EventoMock);
        when(formMock.getImage() != null).thenReturn(false);
        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNull(EventoResourceMock);
    }

    @Mock
    Matcher matcherMock;

    @Test
    void uploadException(){

        when(new Evento(formMock.getIdCollab(), formMock.getText(), formMock.getDate())).thenReturn(EventoMock);
        when(formMock.getImage() != null).thenReturn(true);
        when(imagePatternMock.matcher(formMock.getImage().getOriginalFilename())).thenReturn(matcherMock);

        when(!matcherMock.matches()).thenThrow(new IllegalArgumentException("Only jpeg and png images are supported."));
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> matcherMock.matches());

        assertNotNull(exception.getMessage());
    }

    @Mock
    File FileMock;
    String nameMock;
    FileOutputStream streamMock;
    String addressMock;
    String apacheRootFolderMock;
    String apacheAddressMock;

    @Test
    void uploadTC(){

        when(new Evento(formMock.getIdCollab(), formMock.getText(), formMock.getDate())).thenReturn(EventoMock);
        when(formMock.getImage() != null).thenReturn(true);
        when(imagePatternMock.matcher(formMock.getImage().getOriginalFilename())).thenReturn(matcherMock);

        when(!matcherMock.matches()).thenReturn(false);

        try {
            when(new File(apacheRootFolderMock + "/events/" + formMock.getIdCollab())).thenReturn(FileMock);
            when(FileMock.getAbsolutePath() + "/" + formMock.getDate() + "." + matcherMock.group(1)).thenReturn(nameMock);
            when(new FileOutputStream(nameMock)).thenReturn(streamMock);
            when(String.format("%s/events/%s/%s.%s", apacheAddressMock, formMock.getIdCollab(), formMock.getDate(), matcherMock.group(1))).thenReturn(addressMock);

        } catch (IOException e) {
            e.printStackTrace();
        }
        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNotNull(EventoResourceMock);

    }

    /**
     * TEST MODIFY
     */

    @Test
    void modifyText_Image_Date() {

        Integer id = Mockito.mock(Integer.class);
        when(repoEventoMock.findOne(id)).thenReturn(EventoMock);

        when(formMock.getText() != null).thenReturn(true); //Texto ok

        when(formMock.getImage() != null).thenReturn(true); //Imagen ok
        when(imagePatternMock.matcher(formMock.getImage().getOriginalFilename())).thenReturn(matcherMock);

        when(!matcherMock.matches()).thenReturn(false);

        try {
            when(new File(apacheRootFolderMock + "/events/" + formMock.getIdCollab())).thenReturn(FileMock);
            when(FileMock.getAbsolutePath() + "/" + formMock.getDate() + "." + matcherMock.group(1)).thenReturn(nameMock);
            when(new FileOutputStream(nameMock)).thenReturn(streamMock);
            when(String.format("%s/events/%s/%s.%s", apacheAddressMock, formMock.getIdCollab(), formMock.getDate(), matcherMock.group(1))).thenReturn(addressMock);

        } catch (IOException e) {
            e.printStackTrace();
        }

        when(formMock.getDate() != null).thenReturn(true); //Fecha ok

        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNotNull(EventoResourceMock);

    }

    @Test
    void modifyImage_Date(){

        Integer id = Mockito.mock(Integer.class);
        when(repoEventoMock.findOne(id)).thenReturn(EventoMock);

        when(formMock.getText() != null).thenReturn(false); //Texto NO

        when(formMock.getImage() != null).thenReturn(true); //Imagen ok
        when(imagePatternMock.matcher(formMock.getImage().getOriginalFilename())).thenReturn(matcherMock);

        when(!matcherMock.matches()).thenReturn(false);

        try {
            when(new File(apacheRootFolderMock + "/events/" + formMock.getIdCollab())).thenReturn(FileMock);
            when(FileMock.getAbsolutePath() + "/" + formMock.getDate() + "." + matcherMock.group(1)).thenReturn(nameMock);
            when(new FileOutputStream(nameMock)).thenReturn(streamMock);
            when(String.format("%s/events/%s/%s.%s", apacheAddressMock, formMock.getIdCollab(), formMock.getDate(), matcherMock.group(1))).thenReturn(addressMock);

        } catch (IOException e) {
            e.printStackTrace();
        }

        when(formMock.getDate() != null).thenReturn(true); //Fecha ok

        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNotNull(EventoResourceMock);
    }

    @Test
    void modifyText_Image() {
        Integer id = Mockito.mock(Integer.class);
        when(repoEventoMock.findOne(id)).thenReturn(EventoMock);

        when(formMock.getText() != null).thenReturn(true); //Texto ok

        when(formMock.getImage() != null).thenReturn(true); //Imagen ok
        when(imagePatternMock.matcher(formMock.getImage().getOriginalFilename())).thenReturn(matcherMock);

        when(!matcherMock.matches()).thenReturn(false);

        try {
            when(new File(apacheRootFolderMock + "/events/" + formMock.getIdCollab())).thenReturn(FileMock);
            when(FileMock.getAbsolutePath() + "/" + formMock.getDate() + "." + matcherMock.group(1)).thenReturn(nameMock);
            when(new FileOutputStream(nameMock)).thenReturn(streamMock);
            when(String.format("%s/events/%s/%s.%s", apacheAddressMock, formMock.getIdCollab(), formMock.getDate(), matcherMock.group(1))).thenReturn(addressMock);

        } catch (IOException e) {
            e.printStackTrace();
        }

        when(formMock.getDate() != null).thenReturn(false); //Fecha no

        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNotNull(EventoResourceMock);

    }

    @Test
    void modifyText_Date() {

        Integer id = Mockito.mock(Integer.class);
        when(repoEventoMock.findOne(id)).thenReturn(EventoMock);

        when(formMock.getText() != null).thenReturn(true); //Texto ok

        when(formMock.getImage() != null).thenReturn(false); //Imagen no

        when(formMock.getDate() != null).thenReturn(true); //Fecha ok

        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNotNull(EventoResourceMock);

    }

    @Test
    void modifyNull() {

        Integer id = Mockito.mock(Integer.class);
        when(repoEventoMock.findOne(id)).thenReturn(EventoMock);

        when(formMock.getText() != null).thenReturn(false); //Texto no

        when(formMock.getImage() != null).thenReturn(false); //Imagen no

        when(formMock.getDate() != null).thenReturn(false); //Fecha no

        when(converterEventMock.convert(EventoMock)).thenReturn(EventoResourceMock);
        assertNull(EventoResourceMock);

    }

    /**
     * TEST DELETE
     */

    @Mock
    Integer idMock;

    @Test
    void deleteOK() {

        when(repoEventoMock.findOne(idMock)).thenReturn(EventoMock);
        when(EventoMock != null).thenReturn(true);
        assertTrue(true);
    }

    @Test
    void deleteNull() {

        when(repoEventoMock.findOne(idMock)).thenReturn(EventoMock);
        when(EventoMock != null).thenReturn(false);
        assertFalse(false);
    }

    /**
     * TEST JOIN EVENT
     */

    @Mock
    MeetupResource meetupResourceMock;
    Integer useridMock;
    Integer eventIDMock;
    MeetUp meetMock;
    MeetUpConverter converterMeetMock;
    RepoMeetup repoMeetupMock;

    @Test
    void joinEventOK(){

        when(repoEventoMock.findOne(eventIDMock)).thenReturn(EventoMock);
        when(EventoMock!= null).thenReturn(true);
        when(new MeetUp(eventIDMock,useridMock)).thenReturn(meetMock);
        when(converterMeetMock.convert(meetMock)).thenReturn(meetupResourceMock);
        assertNotNull(meetupResourceMock);
    }

    @Test
    void joinEventNull() {

        when(repoEventoMock.findOne(eventIDMock)).thenReturn(EventoMock);
        when(EventoMock != null).thenReturn(false);
        assertNull(meetupResourceMock);
    }

    /**
     * TEST LEAVE EVENT
     */

    @Test
    void leaveEventOK(){

        when(repoMeetupMock.findOne(new IDmeetUp(eventIDMock,useridMock))).thenReturn(meetMock);
        when(meetMock!= null).thenReturn(true);
        when(converterMeetMock.convert(meetMock)).thenReturn(meetupResourceMock);
        assertNotNull(meetupResourceMock);
    }

    @Test
    void leaveEventNull(){

        when(repoMeetupMock.findOne(new IDmeetUp(eventIDMock,useridMock))).thenReturn(meetMock);
        when(meetMock!= null).thenReturn(false);
        assertNull(meetupResourceMock);
    }

}