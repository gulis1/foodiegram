package main.application.service.manageAccountService;

import main.domain.converter.UsuarioConverter;
import main.domain.resource.UsuarioResource;
import main.persistence.entity.User;
import main.persistence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ManageInfoImpl implements ManageInfo{

    private final UsuarioConverter userConverter = new UsuarioConverter();

    @Autowired
    UserRepo repoUser;

    @Value("${apache.rootFolder}")
    private String apacheRootFolder;

    @Value("${apache.address}")
    private String apacheAddress;

    private final Pattern imagePattern = Pattern.compile(".+\\.(png|jpg|jpeg)$", Pattern.CASE_INSENSITIVE);


    @Override
    public UsuarioResource changeName(Integer idUser, String newName) throws IllegalArgumentException{

        Optional<User> user = repoUser.findById(idUser);

        if(!user.isPresent())
            return null;

        else{
            Optional<User> nameUnique = repoUser.findByName(newName);

            if(!nameUnique.isPresent())//si no hay usuarios con newName, podrá cambiar el nombre
                user.get().setName(newName);
            else{
                throw new IllegalArgumentException("There is already a user with name : " + newName);
            }

            repoUser.save(user.get());
            return userConverter.convert(user);
        }
    }

    @Override
    public UsuarioResource changePasswd(Integer idUser, String newPasswd) {
        Optional<User> user = repoUser.findById(idUser);

        if(!user.isPresent())
            return null;

        else{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.get().setPasswd(encoder.encode(newPasswd));
            repoUser.save(user.get());
            return userConverter.convert(user);
        }
    }

    @Override
    public UsuarioResource changeEmail(Integer idUser, String newMail) {
        Optional<User> user = repoUser.findById(idUser);

        if(!user.isPresent())
            return null;
        else{
            user.get().setEmail(newMail);
            repoUser.save(user.get());
            return userConverter.convert(user);
        }
    }

    @Override
    public UsuarioResource changeProfilePicture(Integer idUser, MultipartFile newProfilePic) throws IOException {

        Optional<User> user = repoUser.findById(idUser);

        if(!user.isPresent())
            return null;

        Matcher matcher = imagePattern.matcher(newProfilePic.getOriginalFilename());

        if (!matcher.matches())
            throw new IllegalArgumentException("Only jpeg and png images are supported.");


        File folder = new File(apacheRootFolder + "/" + user.get().getUserid());
        folder.mkdirs();

        String name = folder.getAbsolutePath() + "/pfp." + matcher.group(1);
        FileOutputStream stream = new FileOutputStream(name);
        stream.write(newProfilePic.getBytes());
        stream.close();

        String address = String.format("%s/%s/pfp.%s", apacheAddress, user.get().getUserid(), matcher.group(1));
        user.get().setImage(address);
        repoUser.save(user.get());

        return userConverter.convert(user);

    }
}
