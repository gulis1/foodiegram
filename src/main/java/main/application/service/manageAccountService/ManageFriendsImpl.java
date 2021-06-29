package main.application.service.manageAccountService;

import main.domain.converter.AmigoConverter;
import main.domain.converter.PreviewPublicacionConverter;
import main.domain.resource.AmigoResource;
import main.domain.resource.PreviewPublicacion;
import main.persistence.IDs.IDamigo;
import main.persistence.entity.Amigo;
import main.persistence.entity.Publicacion;
import main.persistence.entity.Usuario;
import main.persistence.repository.RepoAmigo;
import main.persistence.repository.RepoPublicacion;
import main.persistence.repository.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManageFriendsImpl implements ManageFriends{


    private final AmigoConverter friendConverter = new AmigoConverter();

    PreviewPublicacionConverter converterPreview = new PreviewPublicacionConverter();

    @Autowired
    RepoUsuario repoUser;

    @Autowired
    RepoAmigo repoAmigo;

    @Autowired
    RepoPublicacion repoPost;

    @Override
    public AmigoResource addFriend(Integer id, String name)throws IllegalArgumentException {

        Optional<Usuario> user  = repoUser.findByName(name);

        if(!user.isPresent() || user.get().getId().equals(id)) //comprobamos que el usuario existe
            throw new IllegalArgumentException("There is not a user with name: " + name);

        else{
            List<String> friends = getFriends(id);

            if(!friends.contains(name)){
                Amigo friend = new Amigo(id, user.get().getId());

                repoAmigo.save(friend);
                return friendConverter.convert(Optional.of(friend));

            }
            else
                throw new IllegalArgumentException("You are already friends");

        }
    }

    @Override
    public AmigoResource removeFriend(Integer id, String name) throws IllegalArgumentException{
        Optional<Usuario> user = repoUser.findByName(name);

        if(!user.isPresent())//comprobamos que el usuario existe
            throw new IllegalArgumentException("There is not a user with name: " + name);
        else{
            Optional<Amigo> friend = repoAmigo.findById(new IDamigo(id, user.get().getId())); //comprobamos que son amigos

            if(friend.isPresent()){
                repoAmigo.delete(friend.get());
                return friendConverter.convert(friend);
            }

            else{
                throw new IllegalArgumentException("You have no friend with name: " + name);
            }

        }
    }

    @Override
    public List<String> getFriends(Integer id){
        Optional<Usuario> user = repoUser.findById(id);

        if(!user.isPresent()) return null;
        else{
            List<Amigo> friends = repoAmigo.findAll();
            List<String> friendsName = new ArrayList<>();
            for(Amigo friend : friends){
                if(user.get().getId().equals(friend.getIduser1())){
                    friendsName.add(repoUser.getById(friend.getIduser2()).getName());
                }

            }
            return friendsName;
        }
    }

}
