package main.application.service;

import main.domain.converter.EventoConverter;
import main.domain.converter.MeetUpConverter;
import main.domain.resource.EventoResource;
import main.domain.resource.MeetupResource;
import main.persistence.IDs.IDmeetUp;
import main.persistence.entity.Evento;
import main.persistence.entity.MeetUp;
import main.persistence.repository.RepoEvento;
import main.persistence.repository.RepoMeetup;
import main.rest.forms.EventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private EventoConverter converterEvent = new EventoConverter();

    private MeetUpConverter converterMeet= new MeetUpConverter();

    private final Pattern imagePattern = Pattern.compile(".+\\.(png|jpg|jpeg)$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private RepoEvento repoEvent;

    @Autowired
    private RepoMeetup repoMeetup;

    @Value("${apache.rootFolder}")
    private String apacheRootFolder;

    @Value("${apache.address}")
    private String apacheAddress;


    // BUSCAR EVENTO
    //
    // devuelve una lista con los eventos registrados por el colaborador de idcolab que esten activos
    public List<EventoResource> getEventListByIdcolab(Integer idcolab) {

        List<Evento> userList = repoEvent.findByIdcolab(idcolab);
        return userList.stream().map(x -> converterEvent.convert(Optional.of(x))).collect(Collectors.toList());
    }

    // ORGANIZAR EVENTO
    //
    // crea un evento nuevo con los datos proporcionados
    public EventoResource upload(EventForm form) throws IOException, IllegalArgumentException {

        Evento evnt = new Evento(form.getIdCollab(), form.getText(), form.getDate());
        repoEvent.save(evnt);

        if (form.getImage() != null) {
            Matcher matcher = imagePattern.matcher(form.getImage().getOriginalFilename());

            if (!matcher.matches())
                throw new IllegalArgumentException("Only jpeg and png images are supported.");

            try {
                File folder = new File(apacheRootFolder + "/events/" + form.getIdCollab());
                folder.mkdirs();

                String name = folder.getAbsolutePath() + "/" + form.getDate() + "." + matcher.group(1);
                FileOutputStream stream = new FileOutputStream(name);
                stream.write(form.getImage().getBytes());
                stream.close();

                String address = String.format("%s/events/%s/%s.%s", apacheAddress, form.getIdCollab(), form.getDate(), matcher.group(1));
                evnt.setImage(address);
                repoEvent.save(evnt);
            } catch (IOException e) {
                throw e;
            }
        }

        return converterEvent.convert(java.util.Optional.of(evnt));
    }

    // MODIFICAR EVENTO
    //
    // modifica un evento existente y activo
    public EventoResource modify(Integer id,EventForm form) throws IOException, IllegalArgumentException {

        // encuentra el evento de id
        Optional<Evento> evnt = repoEvent.findById(id);

        if (!evnt.isPresent())
            return null;

        if (form.getText() != null) evnt.get().setText(form.getText());
        if (form.getImage() != null) {
            Matcher matcher = imagePattern.matcher(form.getImage().getOriginalFilename());

            if (!matcher.matches())
                throw new IllegalArgumentException("Only jpeg and png images are supported.");

            try {
                File folder = new File(apacheRootFolder + "/events/" + evnt.get().getIdcolab());
                folder.mkdirs();

                String name = folder.getAbsolutePath() + "/" + form.getDate() + "." + matcher.group(1);
                FileOutputStream stream = new FileOutputStream(name);
                stream.write(form.getImage().getBytes());
                stream.close();

                String address = String.format("%s/events/%s/%s.%s", apacheAddress, evnt.get().getIdcolab(), form.getDate() , matcher.group(1));
                evnt.get().setImage(address);
            } catch (IOException e) {
                throw e;
            }
        }
        if (form.getDate() != null) evnt.get().setEndtime(form.getDate());
        repoEvent.save(evnt.get());

        return converterEvent.convert(evnt);
    }

    // ELIMINAR EVENTO
    //
    // devuelve true si consigue eliminar el evento sino false
    public boolean delete(Integer id) {
        Optional<Evento> evnt = repoEvent.findById(id);

        if (evnt.isPresent()) {
            repoEvent.delete(evnt.get());
            return true;
        }

        return false;
    }

    public MeetupResource joinEvent(Integer userid, Integer eventID){

        Optional<Evento> event= repoEvent.findById(eventID);

        if(event.isPresent()) {

            MeetUp meet=new MeetUp(eventID,userid);
            repoMeetup.save(meet);
            return converterMeet.convert(Optional.of(meet));

        }

        return null;
    }

    public MeetupResource leaveEvent(Integer userid, Integer eventID){

        Optional<MeetUp> meet = repoMeetup.findById(new IDmeetUp(eventID,userid));

        if(meet.isPresent()) {

            repoMeetup.delete(meet.get());
            return converterMeet.convert(meet);

        }

        return null;
    }



}
