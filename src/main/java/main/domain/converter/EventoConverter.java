package main.domain.converter;

import main.domain.resource.EventoResource;
import main.persistence.entity.Evento;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class EventoConverter implements Converter<Optional<Evento>, EventoResource> {

    @Override
    public EventoResource convert(Optional<Evento> source) {
        return source.map(evento -> new EventoResource(evento.getId(), evento.getIdcolab(), evento.getText(), evento.getImage(), evento.getEndtime())).orElse(null);
    }
}
