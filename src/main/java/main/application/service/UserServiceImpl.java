package main.application.service;

import main.domain.converter.*;
import main.domain.resource.PostPreview;
import main.domain.resource.RatingResource;
import main.domain.resource.UserResource;
import main.domain.resource.Usuario_baneadoResource;
import main.persistence.entity.*;
import main.persistence.repository.*;
import main.rest.forms.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private PreviewPublicacionConverter converterPreview = new PreviewPublicacionConverter();
    private ValoracionConverter converterVal = new ValoracionConverter();
    private UsuarioConverter converterUser = new UsuarioConverter();
    private Usuario_baneadoConverter converterBannedUser= new Usuario_baneadoConverter();
    private final MensajeConverter converterMens = new MensajeConverter();


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VerifytokenRepo repoToken;

    @Autowired
    private RepoUsuario_baneado repoUsuario_baneado;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private PostRepo repoPubli;

    @Autowired
    private RatingRepo repoVal;


    @Value("${domain}")
    private String domain;

    private final Random random = new Random();


    @Override
    public UserResource getUserByName(String user) {
        return converterUser.convert(userRepo.findByName(user));
    }

    @Override
    public List<PostPreview> getPosts(String user) {

        Optional<User> usuario = userRepo.findByName(user);

        if (!usuario.isPresent())
            return null;

        else {

            List<Post> publicaciones = repoPubli.findByUser_useridOrderByPostidDesc(usuario.get().getUserid());
            return publicaciones.stream().map(x -> converterPreview.convert(Optional.of(x))).collect(Collectors.toList());
        }
    }

    @Override
    public List<RatingResource> getRatings(String user) {

        Optional<User> usuario = userRepo.findByName(user);

        if (!usuario.isPresent())
            return null;

        else {

            List<Rating> valoracionU = repoVal.findByUser(usuario.get().getUserid());
            return valoracionU.stream().map(x -> converterVal.convert(Optional.of(x))).collect(Collectors.toList());
        }
    }

    @Override
    public UserResource register(UserForm user) throws IllegalArgumentException {

        if(user.getUsername().length()>=20)
            throw new IllegalArgumentException("The name is to long, please insert a name BELOW 20 characters.");

        else if(user.getPassword().length()>=256)
            throw new IllegalArgumentException("The PASSWORD is to long, please insert a password BELOW 20 characters.");


        else if(!user.getEmail().contains("@"))
            throw new IllegalArgumentException("The email introduces is NOT valid, please insert a valid e-mail.");

        else if (userRepo.findByEmail(user.getEmail()).isPresent())
            throw new IllegalArgumentException("That e-mail is already registered.");

        else if (userRepo.findByName(user.getUsername()).isPresent())
            throw new IllegalArgumentException("That username is already taken.");


        else {

            int token;

            // Por si coincide que ese token ya exista (Dificil, pero bueno...)
            do {
                token = random.nextInt(100000000);
            }while(repoToken.findByToken(token).isPresent());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            User newUser = new User(user.getUsername(), encoder.encode(user.getPassword()),"https://new.reactionvrsports.com/wp-content/uploads/2020/11/ACCOUNT.png", user.getEmail());
            userRepo.save(newUser);

            Verifytoken verifyToken=new Verifytoken(user.getEmail(), token);
            repoToken.save(verifyToken);


            // Se envia el email de confirmacion
            String mensaje="Enlace de verificación: " + "http://" + domain + ":8080/users/verify/" + token;
            String topic="Confirmación de correo electrónico en foodiegram.";
            sendEmailService.sendEmails(user.getEmail(), mensaje, topic);


            return converterUser.convert(Optional.of(newUser));

        }
    }

    @Override
    public UserResource verify(Integer token) {//token  de entrada
       //token de entrada comparar token con la id del user
        //
        Optional<Verifytoken> verToken = repoToken.findByToken(token);

        if (!verToken.isPresent())
            return null;

        if(verToken.get().getExpiredate().before(new Date())) {
            repoToken.delete(verToken.get());
            return null;
        }

        Optional<User> newUser = userRepo.findByEmail(verToken.get().getEmail());

        if (!newUser.isPresent())
            return null;

        newUser.get().setEnabled(true);
        newUser.get().setRole(RoleEnum.ROLE_USER);
        userRepo.save(newUser.get());
        repoToken.delete(verToken.get());

        return converterUser.convert(newUser);
    }

    @Override
    public Usuario_baneadoResource banUser(String user, String severity) throws IllegalArgumentException{
        //nombre es unico
        Optional<User> newUser = userRepo.findByName(user);

        if (!newUser.isPresent())
            return null;

        newUser.get().setEnabled(false);
        int maxPenalty=5; //maximo
        int Severity; //local

        try {
             Severity = Integer.parseInt(severity);
        }
        catch(Exception e){
            throw new IllegalArgumentException("the type introduced in severity must be a number between 1-5");
        }

        Date date;
        if(Severity>maxPenalty||Severity<=0)
            return null;


        date=this.calculateDate(Severity);
        Usuario_baneado bannedUser = new Usuario_baneado(newUser.get().getUserid(),date);

        repoUsuario_baneado.save(bannedUser);
        userRepo.save(newUser.get());


        return converterBannedUser.convert(Optional.of(bannedUser));
    }


    @Override
    public Usuario_baneadoResource unbanUser(String user) throws IllegalArgumentException{

        Optional<User> newUser = userRepo.findByName(user);

        if (!newUser.isPresent())
            throw new IllegalArgumentException("Usuario no existe");

        newUser.get().setEnabled(true);

        Optional<Usuario_baneado> bannedUser = repoUsuario_baneado.findById(newUser.get().getUserid());

        if (bannedUser.isPresent()) {
            repoUsuario_baneado.delete(bannedUser.get());
            newUser.get().setEnabled(true);

        }

        return null;

    }


    @Override
    public UserResource deleteUser(String user){

        Optional<User> usuario = userRepo.findByName(user);

        usuario.ifPresent(value -> userRepo.delete(value));

        return converterUser.convert(usuario);
    }

    @Override
    public List<Usuario_baneadoResource> getBannedUserList(){
        List<Usuario_baneado> listabaneado = repoUsuario_baneado.findAll();

        return listabaneado.stream().map(x -> converterBannedUser.convert(Optional.of(x))).collect(Collectors.toList());
    }


    @Override
    public UserResource sendWarning(String user, Integer type){

        Optional<User> usuario = userRepo.findByName(user);

        if (usuario.isPresent()) {
            String email= usuario.get().getEmail();
            Date currentDate = new Date();
            String mensaje = getWarningMessage(type)+", fecha de la operacion: " + currentDate;
            String topic="AVISO: COMETISTES UNA INFRACCION EN FOODIEGRAM";
            sendEmailService.sendEmails(email, mensaje, topic);
        }

        return converterUser.convert(usuario);
    }

    private String getWarningMessage(Integer num) {
        String message="";

        switch(num){
            case 1:  //imagenes no apropiados
                message="Has subido fotos que incumplen con la normativa de foodiegram";
                message=message+", Este es tu primer aviso,al siguiente baneo";
                break;
            case 2://lenguaje ofensivo en comentarios
                message="Has empleado lenguaje inapropiado a la hora de comentar en la plataforma";
                message=message+", Este es tu primer aviso,al siguiente baneo";
                break;
            case 3://baneo + aviso de infraccion
                message="Has sido baneado,habiendo incumplido con las normativas de la plataforma mas de una vez";

        }

        return message;
    }

    private Date calculateDate(Integer severity) {

        Calendar cal = Calendar.getInstance();
        System.out.println(cal.getTime().getTime());
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        switch(severity){
            case 1:
                cal.add(Calendar.HOUR,24); //1 day
                break;
            case 2:
                cal.add(Calendar.HOUR,168); //1 week
                break;
            case 3:
                cal.add(Calendar.MONTH,1); //1 month
                break;
            case 4:
                cal.add(Calendar.MONTH,6); //6 month
                break;
            case 5:
                cal.add(Calendar.YEAR,50); //permanent
                break;

        }

        return new Date(cal.getTime().getTime());
    }

}
