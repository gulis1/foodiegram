package main.application.service;

import main.domain.converter.MensajeConverter;
import main.domain.resource.MensajeResource;
import main.persistence.entity.Mensaje;
import main.persistence.entity.Usuario;
import main.persistence.repository.RepoMensaje;
import main.persistence.repository.RepoUsuario;
import main.rest.forms.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MensajeServiceImpl implements MensajeService{


    private final MensajeConverter converterMens = new MensajeConverter();

    @Autowired
    RepoMensaje repoMens;

    @Autowired
    RepoUsuario repoUser;


    @Override
    public MensajeResource deleteMensaje(Integer mensID) throws NoPermissionException {
        Optional<Mensaje> mens = repoMens.findById(mensID);

        mens.ifPresent(mensaje -> repoMens.delete(mensaje));

        return converterMens.convert(mens);

    }

    @Override
    public MensajeResource setMensaje(Integer userID, MessageForm mensaje) throws IllegalArgumentException {

        Optional<Usuario> user2 = repoUser.findByName(mensaje.getReceiver());

        if (!user2.isPresent())
            throw new IllegalArgumentException("That user does not exist.");


        Mensaje mens = new Mensaje(userID, user2.get().getId(), mensaje.getText());
        repoMens.save(mens);

        return converterMens.convert(Optional.of(mens));

    }

    @Override
    public List<MensajeResource> getMensajes(Integer userID) {


        Optional<Usuario> user = repoUser.findById(userID);

        if (user.isPresent()) {
            List<Mensaje> mensajes = repoMens.findByIduser1OrIduser2(user.get().getId(), user.get().getId());
            return mensajes.stream().map(mensaje -> converterMens.convert(Optional.of(mensaje))).collect(Collectors.toList());
        }

        else return null;


    }

}
