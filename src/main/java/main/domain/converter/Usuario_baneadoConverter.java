package main.domain.converter;

import main.domain.resource.Usuario_baneadoResource;
import main.persistence.entity.Usuario_baneado;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Usuario_baneadoConverter implements Converter<Optional<Usuario_baneado>, Usuario_baneadoResource> {
        @Override
        public Usuario_baneadoResource convert (Optional<Usuario_baneado> source){

            if(!source.isPresent()){
                return null;
            }
            Usuario_baneadoResource response=new Usuario_baneadoResource();
            response.setDate(source.get().getDate());
            response.setId(source.get().getId());
            return response;

        }


}
