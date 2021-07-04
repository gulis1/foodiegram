package main.application.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import main.domain.converter.ColaboradorConverter;
import main.domain.resource.ColaboradorResource;

import main.persistence.entity.Colaborador;
import main.persistence.entity.Usuario;
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.UserRepo;

import main.rest.forms.CollaborateForm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import java.util.Map;



@ExtendWith(MockitoExtension.class)

class ColaboradorServiceImplTest {

    @Mock
    ColaboradorResource ColaboradorResourceMock;
    Integer useridMock;
    CollaborateForm formMock;
    RepoUsuario repoUsuarioMock;
    RestService restServiceMock;
    ColaboradorConverter converterColMock;
    String country;
    String city;
    String street;
    Usuario userMock;
    Map<String, Object> geoDataMock;
    Colaborador colaboradorMock;
    RepoColaborador repoColaboradorMock;

    /**
     * TEST UPGRADE USER
     */

    @Test
    void upgradeUserOK() {
        when(repoUsuarioMock.findOne(useridMock)).thenReturn(userMock);
        when(formMock.getLatitud() != null && formMock.getLongitud() != null).thenReturn(true);
        when(restServiceMock.getGeoData(formMock.getLatitud(), formMock.getLongitud())).thenReturn(geoDataMock);

        try {
            when(geoDataMock.get("country").toString()).thenReturn(country);
            when(geoDataMock.get("city").toString()).thenReturn(city);
            when(geoDataMock.get("street").toString()).thenReturn(street);

        }
        catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }

        when(new Colaborador(userMock.getId(), formMock.getOrigin(), formMock.getType(), country,city,street)).thenReturn(colaboradorMock);
        when(converterColMock.convert(colaboradorMock)).thenReturn(ColaboradorResourceMock);
        assertNotNull(ColaboradorResourceMock);

    }

    @Test
    void upgradeUserNull() {
        when(repoUsuarioMock.findOne(useridMock)).thenReturn(userMock);
        when(formMock.getLatitud() != null && formMock.getLongitud() != null).thenReturn(false);

        when(new Colaborador(userMock.getId(), formMock.getOrigin(), formMock.getType(), country,city,street)).thenReturn(colaboradorMock);
        when(converterColMock.convert(colaboradorMock)).thenReturn(ColaboradorResourceMock);
        assertNull(ColaboradorResourceMock);

    }

    /**
     * TEST GET COLLAB
     */

    @Mock
    Integer User;

    @Test
    void getCollab() {
        when(converterColMock.convert(repoColaboradorMock.findOne(User))).thenReturn(ColaboradorResourceMock);
        assertNotNull(ColaboradorResourceMock);
    }
}