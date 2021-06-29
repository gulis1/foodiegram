package main.application.service;

import main.domain.converter.ComentarioConverter;
import main.domain.converter.PublicacionConverter;
import main.domain.converter.ValoracionConverter;
import main.domain.resource.ComentarioResource;
import main.domain.resource.PublicacionResource;
import main.domain.resource.ValoracionResource;
import main.persistence.IDs.IDvaloracion;
import main.persistence.entity.Comentario;
import main.persistence.entity.Publicacion;
import main.persistence.entity.Usuario;
import main.persistence.entity.Valoracion;
import main.persistence.repository.RepoComentario;
import main.persistence.repository.RepoPublicacion;
import main.persistence.repository.RepoUsuario;
import main.persistence.repository.RepoValoracion;
import main.rest.forms.CommentForm;
import main.rest.forms.PostForm;
import main.rest.forms.RatingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private RepoPublicacion repoPubli;

    @Autowired
    private RepoValoracion repoVal;

    @Autowired
    private RepoComentario repoComen;

    @Autowired
    private RepoUsuario repoUsuario;

    @Autowired
    private RestService restService;

    @Value("${apache.address}")
    private String apacheAddress;

    @Value("${apache.rootFolder}")
    private String apacheRootFolder;

    @Override
    public PublicacionResource getPost(Integer pubID) {
        return converterPubli.convert(repoPubli.findById(pubID));
    }

    @Override
    public PublicacionResource editPost(Integer pubID, String text) throws IllegalArgumentException, NoPermissionException {

        if (text == null)
            throw new IllegalArgumentException("Text or loc should be not null");


        Optional<Publicacion> publi = repoPubli.findById(pubID);

        if (publi.isPresent()) {

            publi.get().setText(text);


            repoPubli.save(publi.get());

        }

        return converterPubli.convert(publi);

    }

    @Override
    public PublicacionResource deletePost(Integer pubID) throws NoPermissionException {

        Optional<Publicacion> publi = repoPubli.findById(pubID);

        publi.ifPresent(publicacion -> repoPubli.delete(publicacion));


        return converterPubli.convert(publi);


    }

    @Override
    public PublicacionResource upload(Integer userID, PostForm form) throws IOException, IllegalArgumentException {
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

        Publicacion publi = new Publicacion(form.getText(), userID, country, city);
        publi = repoPubli.save(publi);

        if (form.getImage() != null) {

            Matcher matcher = imagePattern.matcher(form.getImage().getOriginalFilename());

            if (!matcher.matches())
                throw new IllegalArgumentException("Only jpeg and png images are supported.");

            // Se crea una publicacion sin imagen

            try {
                File folder = new File(apacheRootFolder + "/users/" + userID);
                folder.mkdirs();

                String name = folder.getAbsolutePath() + "/" + publi.getId() + "." + matcher.group(1);
                FileOutputStream stream = new FileOutputStream(name);
                stream.write(form.getImage().getBytes());
                stream.close();

                // Si se ha conseguido guardar la imagen, se le asocia a la publicacion una direccion en la BD.
                String address = String.format("%s/users/%s/%s.%s", apacheAddress, userID, publi.getId(), matcher.group(1));
                publi.setImage(address);
                repoPubli.save(publi);
            } catch (IOException e) {
                repoPubli.delete(publi);
                throw e;
            }

        }


        return converterPubli.convert(Optional.of(publi));
    }

    @Override
    public List<ValoracionResource> getRatings(Integer pubID) {

        Optional<Publicacion> publi = repoPubli.findById(pubID);

        if (!publi.isPresent())
            return null;

        else {
            List<Valoracion> valoraciones = repoVal.findByIdpubli(pubID);
            return valoraciones.stream().map(x -> converterVal.convert(Optional.of(x))).collect(Collectors.toList());
        }
    }

    @Override
    public ValoracionResource setRating(RatingForm form) throws IllegalArgumentException {

        if (form.getScore() < 0 || form.getScore() > 5)
            throw new IllegalArgumentException("Punt must be a integer between 0 and 5");

        else {
            Valoracion valora = new Valoracion(form.getPubID(), form.getUserID(), form.getScore());
            repoVal.save(valora);

            return converterVal.convert(Optional.of(valora));
        }

    }

    @Override
    public ValoracionResource getRating(Integer pubID, String user) {

        Optional<Usuario> usuario = repoUsuario.findByName(user);

        return usuario.map(value -> converterVal.convert(repoVal.findById(new IDvaloracion(pubID, value.getId())))).orElse(null);

    }

    @Override
    public ValoracionResource deleteRating(RatingForm form) {

        Optional<Valoracion> valor = repoVal.findById(new IDvaloracion(form.getPubID(), form.getUserID()));

        valor.ifPresent(valoracion -> repoVal.delete(valoracion));

        return converterVal.convert(valor);
    }

    @Override
    public List<ComentarioResource> getComments(Integer pubID) {

        Optional<Publicacion> pub = repoPubli.findById(pubID);

        if (!pub.isPresent())
            return null;

        else {

            List<Comentario> comentarios = repoComen.findByIdpubliOrderByIdAsc(pubID);
            return comentarios.stream().map(x -> converterCom.convert(Optional.of(x))).collect(Collectors.toList());
        }

    }

    @Override
    public ComentarioResource setComment( CommentForm form) throws IllegalArgumentException {

        if (form.getText() == null || form.getText().length() == 0)
            throw new IllegalArgumentException("Text must be not null");


        Comentario comment = new Comentario(form.getPubID(), new Usuario(form.getUserID()), form.getText());
        repoComen.save(comment);
        return converterCom.convert(Optional.of(comment));

    }

}

