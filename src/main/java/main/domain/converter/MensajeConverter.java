package main.domain.converter;

import main.domain.resource.MessageResource;
import main.persistence.entity.Message;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MensajeConverter implements Converter<Optional<Message>, MessageResource> {

    @Override
    public MessageResource convert(Optional<Message> source){

        if (!source.isPresent())
            return null;

        MessageResource response = new MessageResource();
        response.setId(source.get().getMessageid());
        response.setIduser1(source.get().getSender());
        response.setIduser2(source.get().getReceiver());
        response.setText(source.get().getText());
        return response;
    }


}