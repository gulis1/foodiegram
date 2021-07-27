package main.application.service;

import main.domain.converter.ComentarioConverter;
import main.domain.converter.PublicacionConverter;
import main.domain.converter.ValoracionConverter;
import main.domain.resource.CommentResource;
import main.domain.resource.PostResource;
import main.domain.resource.RatingResource;
import main.persistence.IDs.RatingID;
import main.persistence.entity.Comment;
import main.persistence.entity.Post;
import main.persistence.entity.Rating;
import main.persistence.entity.User;
import main.persistence.repository.CommentRepo;
import main.persistence.repository.PostRepo;
import main.persistence.repository.UserRepo;
import main.persistence.repository.RatingRepo;
import main.rest.forms.CommentForm;
import main.rest.forms.PostForm;
import main.rest.forms.RatingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class
PublicationServiceImpl implements PublicationService {

    private final PublicacionConverter converterPubli = new PublicacionConverter();

    private final ValoracionConverter converterVal = new ValoracionConverter();

    private final ComentarioConverter converterCom = new ComentarioConverter();

    private final Pattern imagePattern = Pattern.compile(".+\\.(png|jpg|jpeg)$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private PostRepo repoPubli;

    @Autowired
    private RatingRepo repoVal;

    @Autowired
    private CommentRepo repoComen;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestService restService;

    @Value("${apache.address}")
    private String apacheAddress;

    @Value("${apache.rootFolder}")
    private String apacheRootFolder;

    @Override
    public PostResource getPost(Integer pubID) {
        return converterPubli.convert(repoPubli.findById(pubID));
    }

    @Override
    public PostResource editPost(Integer pubID, String text) throws IllegalArgumentException, NoPermissionException {

        if (text == null)
            throw new IllegalArgumentException("Text or loc should be not null");


        Optional<Post> publi = repoPubli.findById(pubID);

        if (publi.isPresent()) {

            publi.get().setText(text);


            repoPubli.save(publi.get());

        }

        return converterPubli.convert(publi);

    }

    @Override
    public PostResource deletePost(Integer pubID) throws NoPermissionException {

        Optional<Post> publi = repoPubli.findById(pubID);

        publi.ifPresent(publicacion -> repoPubli.delete(publicacion));


        return converterPubli.convert(publi);


    }

    @Override
    public PostResource upload(Integer userID, PostForm form) throws IOException, IllegalArgumentException {

        Matcher matcher = imagePattern.matcher(form.getImage().getOriginalFilename());

        if (!matcher.matches())
            throw new IllegalArgumentException("Only jpeg and png images are supported.");

        String country = null;
        String city = null;

        if (form.getLatitud() != null && form.getLongitud() != null) {
            Map<String, Object> geoData = restService.getGeoData(form.getLatitud(), form.getLongitud());
            try {
                country = geoData.get("country").toString();
                city = geoData.get("city").toString();
            }

            catch (NullPointerException ignored) {

            }
        }

        Post publi = new Post(form.getTitle(), form.getText(), new User(userID), country, city);
        publi = repoPubli.save(publi);

        try {
            File folder = new File(apacheRootFolder + "/users/" + userID);
            folder.mkdirs();

            String name = folder.getAbsolutePath() + "/" + publi.getPostid() + "." + matcher.group(1);
            FileOutputStream stream = new FileOutputStream(name);
            stream.write(form.getImage().getBytes());
            stream.close();
            // Si se ha conseguido guardar la imagen, se le asocia a la publicacion una direccion en la BD.

            String address = String.format("%s/users/%s/%s.%s", apacheAddress, userID, publi.getPostid(), matcher.group(1));

            publi.setImage(address);
            repoPubli.save(publi);
        }

        catch (IOException e) {
            repoPubli.delete(publi);
            throw e;
        }




        return converterPubli.convert(Optional.of(publi));
    }

    @Override
    public List<RatingResource> getRatings(Integer pubID) {

        Optional<Post> publi = repoPubli.findById(pubID);

        if (!publi.isPresent())
            return null;

        else {
            List<Rating> valoraciones = repoVal.findByPost(pubID);
            return valoraciones.stream().map(x -> converterVal.convert(Optional.of(x))).collect(Collectors.toList());
        }
    }

    @Override
    public RatingResource setRating(RatingForm form) throws IllegalArgumentException {

        if (form.getScore() < 0 || form.getScore() > 5)
            throw new IllegalArgumentException("Punt must be a integer between 0 and 5");

        else {
            Rating valora = new Rating(form.getPubID(), form.getUserID(), form.getScore());
            repoVal.save(valora);

            return converterVal.convert(Optional.of(valora));
        }

    }

    @Override
    public RatingResource getRating(Integer pubID, String user) {

        Optional<User> usuario = userRepo.findByName(user);

        return usuario.map(value -> converterVal.convert(repoVal.findById(new RatingID(pubID, value.getUserid())))).orElse(null);

    }

    @Override
    public RatingResource deleteRating(RatingForm form) {

        Optional<Rating> valor = repoVal.findById(new RatingID(form.getPubID(), form.getUserID()));

        valor.ifPresent(valoracion -> repoVal.delete(valoracion));

        return converterVal.convert(valor);
    }

    @Override
    public List<CommentResource> getComments(Integer pubID) {

        Optional<Post> pub = repoPubli.findById(pubID);

        if (!pub.isPresent())
            return null;

        else {

            List<Comment> comentarios = repoComen.findByPostOrderByCommentidAsc(pubID);
            return comentarios.stream().map(x -> converterCom.convert(Optional.of(x))).collect(Collectors.toList());
        }

    }

    @Override
    public CommentResource setComment(CommentForm form) throws IllegalArgumentException {

        if (form.getText() == null || form.getText().length() == 0)
            throw new IllegalArgumentException("Text must be not null");


        Comment comment = new Comment(form.getPubID(), new User(form.getUserID()), form.getText());
        repoComen.save(comment);
        return converterCom.convert(Optional.of(comment));

    }

}

