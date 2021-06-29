package main.application.service.manageAccountService;

import main.domain.converter.PreviewPublicacionConverter;
import main.domain.converter.PublicacionConverter;
import main.domain.resource.PreviewPublicacion;
import main.persistence.entity.Publicacion;
import main.persistence.entity.Usuario;
import main.persistence.repository.RepoPublicacion;
import main.persistence.repository.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViewImagesImpl implements  ViewImages{

    private final PublicacionConverter postConverter = new PublicacionConverter();
    PreviewPublicacionConverter converterPreview = new PreviewPublicacionConverter();

    @Autowired
    RepoPublicacion repoPost;

    @Autowired
    RepoUsuario repoUser;

    @Override
    public List<PreviewPublicacion> viewPost(Integer idUser) {

        Optional<Usuario> user = repoUser.findById(idUser);

        if(!user.isPresent()) //comprobamos que existe el usuario con idUser
            return null;
        else{
            List<Publicacion> post = repoPost.findByIduserOrderByIdDesc(idUser);
            return post.stream().map(x -> converterPreview.convert(Optional.of(x))).collect(Collectors.toList());
        }
    }
}
