package main.application.service;

import main.domain.converter.MensajeConverter;
import main.domain.resource.MessageResource;
import main.persistence.entity.Message;
import main.persistence.entity.User;
import main.persistence.repository.MessageRepo;
import main.persistence.repository.UserRepo;
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
    MessageRepo repoMens;

    @Autowired
    UserRepo repoUser;


    @Override
    public MessageResource deleteMensaje(Integer mensID) throws NoPermissionException {
        Optional<Message> mens = repoMens.findById(mensID);

        mens.ifPresent(message -> repoMens.delete(message));

        return converterMens.convert(mens);

    }

    @Override
    public MessageResource setMensaje(Integer userID, MessageForm mensaje) throws IllegalArgumentException {

        Optional<User> user2 = repoUser.findByName(mensaje.getReceiver());

        if (!user2.isPresent())
            throw new IllegalArgumentException("That user does not exist.");


        Message mens = new Message(userID, user2.get().getUserid(), mensaje.getText());
        repoMens.save(mens);

        return converterMens.convert(Optional.of(mens));

    }

    @Override
    public List<MessageResource> getMensajes(Integer userID) {


        Optional<User> user = repoUser.findById(userID);

        if (user.isPresent()) {
            List<Message> messages = repoMens.findBySenderOrReceiver(user.get().getUserid(), user.get().getUserid());
            return messages.stream().map(message -> converterMens.convert(Optional.of(message))).collect(Collectors.toList());
        }

        else return null;


    }

}
