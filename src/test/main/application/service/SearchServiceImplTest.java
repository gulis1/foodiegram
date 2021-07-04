package main.application.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import main.domain.converter.PreviewColabJOINUserConverter;
import main.domain.converter.PreviewUserConverter;
import main.domain.resource.PreviewColabJOINUser;
import main.domain.resource.PreviewPubliJOINUser;
import main.domain.resource.PreviewUsuario;

import main.persistence.entity.ColabJOINUser;
import main.persistence.entity.Usuario;
import main.persistence.repository.UserRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)

class SearchServiceImplTest {

    /**
     * TEST GET USER LIST
     */

    @Mock
    List<PreviewUsuario> PrevUsuarioMock;
    List<Usuario> userListMock;
    RepoUsuario repoUserMock;
    PreviewUserConverter converterPrevUsuarioMock;

    @Test
    void getUserList() {

        String userName = "Ahsoka";
        when(repoUserMock.findBynameContainingIgnoreCase(userName)).thenReturn(userListMock);
        when(userListMock.stream().map(converterPrevUsuarioMock::convert).collect(Collectors.toList())).thenReturn(PrevUsuarioMock);
        assertNotNull(PrevUsuarioMock);

    }

    /**
     * TEST GET COLAB LIST BY NAME
     */

    @Mock
    List<PreviewColabJOINUser> ColabListByNameMock;
    List<ColabJOINUser> colabJuserMock;
    RepoColabJOINUser RepoColabJOINUserMock;
    PreviewColabJOINUserConverter PreviewColabJOINUserConverterMock;

    @Test
    void getColabListByName() {

        String colabMock = "Mos Eisley";
        when(RepoColabJOINUserMock.findByUsername(colabMock)).thenReturn(colabJuserMock);
        when(colabJuserMock.stream().map(PreviewColabJOINUserConverterMock::convert).collect(Collectors.toList())).thenReturn(ColabListByNameMock);
        assertNotNull(ColabListByNameMock);

    }

    /**
     * TEST GET COLAB LIST BY ORIGIN
     */

    @Mock
    List<PreviewColabJOINUser> ColabListByOriginMock;

    @Test
    void getColabListByOrigin() {

        String origin = "Tatooine";
        when(RepoColabJOINUserMock.findByOrigin(origin)).thenReturn(colabJuserMock);
        when(colabJuserMock.stream().map(PreviewColabJOINUserConverterMock::convert).collect(Collectors.toList())).thenReturn(ColabListByOriginMock);
        assertNotNull(ColabListByOriginMock);

    }

    /**
     * TEST GET COLAB LIST BY TYPE
     */

    @Mock
    List<PreviewColabJOINUser> ColabListByTypeMock;

    @Test
    void getColabListByType() {

        String type = "Veggie";
        when(RepoColabJOINUserMock.findByType(type)).thenReturn(colabJuserMock);
        when(colabJuserMock.stream().map(PreviewColabJOINUserConverterMock::convert).collect(Collectors.toList())).thenReturn(ColabListByTypeMock);
        assertNotNull(ColabListByTypeMock);

    }

    /**
     * TEST GET PUBLI LIST BY TAG
     */

    @Mock
    List<PreviewPubliJOINUser> publiListByTagMock;
    List<PubliJOINUser> publiJuserMock;
    RepoPubliJOINUser repoPubliJUserMock;
    PreviewPubliJOINUserConverter converterPreviewPubliJUserMock;

    @Test
    void getPubliListByTag(){

        String tag = "Cheap";
        when(repoPubliJUserMock.findByTag(tag)).thenReturn(publiJuserMock);
        when(publiJuserMock.stream().map(converterPreviewPubliJUserMock::convert).collect(Collectors.toList())).thenReturn(publiListByTagMock);
        assertNotNull(publiListByTagMock);

    }

    /**
     * TEST GET USERLIST BY PUBLI
     */

    @Test
    void getUserListByPubliOk(){

        when(repoUserMock.findByPopuPubli()).thenReturn(userListMock);
        when(userListMock.stream().map(converterPrevUsuarioMock::convert).collect(Collectors.toList())).thenReturn(PrevUsuarioMock);
        assertNotNull(PrevUsuarioMock);

    }

    @Test
    void getUserListByPubliNull(){

        PrevUsuarioMock = null;

        when(repoUserMock.findByPopuPubli()).thenReturn(userListMock);
        when(userListMock.stream().map(converterPrevUsuarioMock::convert).collect(Collectors.toList())).thenReturn(PrevUsuarioMock);
        assertNull(PrevUsuarioMock);

    }

    /**
     * TEST GET USER LIST BY VALORATION
     */

    @Test
    void getUserListByValOk(){

        when(repoUserMock.findByPopuVal()).thenReturn(userListMock);
        when(userListMock.stream().map(converterPrevUsuarioMock::convert).collect(Collectors.toList())).thenReturn(PrevUsuarioMock);
        assertNotNull(PrevUsuarioMock);

    }

    @Test
    void getUserListByValNull(){

        when(repoUserMock.findByPopuVal()).thenReturn(userListMock);
        when(userListMock.stream().map(converterPrevUsuarioMock::convert).collect(Collectors.toList())).thenReturn(PrevUsuarioMock);
        assertNull(PrevUsuarioMock);

    }

}