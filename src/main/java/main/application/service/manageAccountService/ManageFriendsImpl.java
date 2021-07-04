package main.application.service.manageAccountService;

import main.domain.converter.AmigoConverter;
import main.domain.converter.PreviewPublicacionConverter;
import main.domain.resource.AmigoResource;
import main.persistence.IDs.FollowID;
import main.persistence.entity.Follow;
import main.persistence.entity.User;
import main.persistence.repository.FollowRepo;
import main.persistence.repository.PostRepo;
import main.persistence.repository.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManageFriendsImpl implements ManageFriends{


    private final AmigoConverter friendConverter = new AmigoConverter();

    PreviewPublicacionConverter converterPreview = new PreviewPublicacionConverter();

    @Autowired
    RepoUsuario repoUser;

    @Autowired
    FollowRepo followRepo;

    @Autowired
    PostRepo repoPost;

    @Override
    public AmigoResource addFriend(Integer id, String name)throws IllegalArgumentException {

        Optional<User> user  = repoUser.findByName(name);

        if(!user.isPresent() || user.get().getUserid().equals(id)) //comprobamos que el usuario existe
            throw new IllegalArgumentException("There is not a user with name: " + name);

        else{
            List<String> friends = getFriends(id);

            if(!friends.contains(name)){
                Follow friend = new Follow(id, user.get().getUserid());

                followRepo.save(friend);
                return friendConverter.convert(Optional.of(friend));

            }
            else
                throw new IllegalArgumentException("You are already friends");

        }
    }

    @Override
    public AmigoResource removeFriend(Integer id, String name) throws IllegalArgumentException{
        Optional<User> user = repoUser.findByName(name);

        if(!user.isPresent())//comprobamos que el usuario existe
            throw new IllegalArgumentException("There is not a user with name: " + name);
        else{
            Optional<Follow> friend = followRepo.findById(new FollowID(id, user.get().getUserid())); //comprobamos que son amigos

            if(friend.isPresent()){
                followRepo.delete(friend.get());
                return friendConverter.convert(friend);
            }

            else{
                throw new IllegalArgumentException("You have no friend with name: " + name);
            }

        }
    }

    @Override
    public List<String> getFriends(Integer id){
        Optional<User> user = repoUser.findById(id);

        if(!user.isPresent()) return null;
        else{
            List<Follow> friends = followRepo.findAll();
            List<String> friendsName = new ArrayList<>();
            for(Follow friend : friends){
                if(user.get().getUserid().equals(friend.getFollower())){
                    friendsName.add(repoUser.getOne(friend.getFollowed()).getName());
                }

            }
            return friendsName;
        }
    }

}
