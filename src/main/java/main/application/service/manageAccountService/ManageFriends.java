package main.application.service.manageAccountService;


import main.domain.resource.FollowResource;

import java.util.List;

public interface ManageFriends {

    //El usuario con id añade un usario de nombre name, devuelve null en caso de fallo
    FollowResource addFriend(Integer id, String name);

    //El usuario con id elimina al usuario con nombre name de su lista de amigos, devuelve una excepción en caso de fallo
    FollowResource removeFriend(Integer id, String name);

    //Devuelve la lista de amigos
    List<String> getFriends(Integer id);

}
