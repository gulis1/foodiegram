package main.application.service.manageAccountService;

import main.domain.converter.PreviewPublicacionConverter;
import main.domain.converter.PublicacionConverter;
import main.domain.resource.PostPreview;
import main.persistence.entity.Post;
import main.persistence.entity.User;
import main.persistence.repository.PostRepo;
import main.persistence.repository.UserRepo;
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
    PostRepo repoPost;

    @Autowired
    UserRepo repoUser;

    @Override
    public List<PostPreview> viewPost(Integer idUser) {

        Optional<User> user = repoUser.findById(idUser);

        if(!user.isPresent()) //comprobamos que existe el usuario con idUser
            return null;
        else{
            List<Post> post = repoPost.findByUser_useridOrderByPostidDesc(idUser);
            return post.stream().map(x -> converterPreview.convert(Optional.of(x))).collect(Collectors.toList());
        }
    }
}
