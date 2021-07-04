package main.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import main.domain.converter.*;
import main.domain.resource.PreviewColabJOINUser;
import main.domain.resource.PreviewPublicacion;
import main.domain.resource.PreviewUsuario;

import main.persistence.entity.ColabJOINUser;
import main.persistence.entity.Publicacion;
import main.persistence.entity.Usuario;
import main.persistence.repository.PostRepo;
import main.persistence.repository.RepoUsuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)

class DiscoverServiceImplTest {


    @Mock
    List<PreviewPublicacion> ListPreviewPublicacionMock;
    RepoPublicacion RepoPublicacionMock;
    PreviewPublicacionConverter PubliConverterMock;
    List<Publicacion> ListPublicacionMock;

    /**
     *  TEST DISCOVER BY AMIGO
     */
    @Test
    void discoverByAmigoTestNull() {

        Integer useridMock = Mockito.mock(Integer.class);

        when(Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName())).thenReturn(useridMock);
        when(RepoPublicacionMock.fromFriends(useridMock)).thenReturn(ListPublicacionMock);
        when(ListPublicacionMock == null).thenReturn(true);

        assertNull(ListPreviewPublicacionMock);

    }

    @Test
    void discoverByAmigoTestOK() {

        Integer useridMock = Mockito.mock(Integer.class);

        when(Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName())).thenReturn(useridMock);
        when(RepoPublicacionMock.fromFriends(useridMock)).thenReturn(ListPublicacionMock);
        when(ListPublicacionMock == null).thenReturn(false);

        when(ListPublicacionMock.stream().map(PubliConverterMock::convert).collect(Collectors.toList())).thenReturn(ListPreviewPublicacionMock);
        assertNotNull(ListPreviewPublicacionMock);

    }

    /**
     *  TEST FIND FOLLOWED BY FRIENDS
     */

    @Mock

    RepoUsuario repoUsuarioMock;
    PreviewUserConverter usuarioConverterMock;
    Usuario usuarioMock;
    List<PreviewUsuario> ListPreviewUserMock;
    List<Usuario> ListUsuarioMock;

    @Test
    void findFollowedByFriendsTestOk() {

        Integer userid = Mockito.mock(Integer.class);

       when(repoUsuarioMock.findFollowedByFriends(userid)).thenReturn(ListUsuarioMock);
       when(ListUsuarioMock.stream().map(usuarioConverterMock::convert).collect(Collectors.toList())).thenReturn(ListPreviewUserMock);

       assertNotNull(ListPreviewUserMock);

    }



    /**
     *  TEST USER WHO FOLLOW X ALSO FOLLOW Y
     */

    @Test
    void userWhoFollowXAlsoFollowYTestNull() {

        String userMock = Mockito.mock(String.class);

        when(repoUsuarioMock.findByName(userMock)).thenReturn(usuarioMock);
        when(usuarioMock == null).thenReturn(true);

        assertNull(ListPreviewPublicacionMock);

    }


    @Test
    void userWhoFollowXAlsoFollowYTestOK() {

        String userMock = Mockito.mock(String.class);

        when(repoUsuarioMock.findByName(userMock)).thenReturn(usuarioMock);
        when(usuarioMock == null).thenReturn(false);

        when(repoUsuarioMock.usersFollowedByUsersWhoFollow(usuarioMock.getId())).thenReturn(ListUsuarioMock);
        when(ListUsuarioMock.stream().map(usuarioConverterMock::convert).collect(Collectors.toList())).thenReturn(ListPreviewUserMock);
        assertNotNull(ListPreviewUserMock);

    }

    /**
     *  TEST FIND COLLABS
     */

    @Mock
    RepoColabJOINUser repoColabJOINUserMock;
    List<ColabJOINUser> ListColabJOINUserMock;
    List<PreviewColabJOINUser>  ListPreviewColabJOINUserMock;
    PreviewColabJOINUserConverter PreviewColabJOINUserConverterMock;

    @Test
    void findCollabs() {

        String countryMock = Mockito.mock(String.class);
        String cityMock = Mockito.mock(String.class);

        when(repoColabJOINUserMock.descubrirCollab(countryMock,cityMock)).thenReturn(ListColabJOINUserMock);
        when(ListColabJOINUserMock.stream().map(PreviewColabJOINUserConverterMock::convert).collect(Collectors.toList())).thenReturn(ListPreviewColabJOINUserMock);

        assertNotNull(ListPreviewColabJOINUserMock);

    }
}