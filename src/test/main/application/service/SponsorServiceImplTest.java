package main.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import main.domain.converter.PatrocinioConverter;
import main.domain.resource.PatrocinioResource;

import main.persistence.entity.Colaborador;
import main.persistence.entity.Patrocinio;
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.SponsorRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)

class SponsorServiceImplTest {

    /**
     * TEST GET SPONSOR SHIP
     */

    @Mock
    PatrocinioResource patrocinioResourceMock;
    PatrocinioConverter patrocinioConverterMock;
    RepoPatrocinio repoPatrocinioMock;
    RepoColaborador repoColaboradorMock;
    Integer idMock;
    Float moneyMock;
    Colaborador colabMock;

    @Test
    void getSponsorshipTestOk() {

        when(patrocinioConverterMock.convert(repoPatrocinioMock.findById(idMock))).thenReturn(patrocinioResourceMock);
        assertNotNull(patrocinioResourceMock);

    }

    @Test
    void getSponsorshipTestNull() {

        patrocinioResourceMock = null;

        when(patrocinioConverterMock.convert(repoPatrocinioMock.findById(idMock))).thenReturn(patrocinioResourceMock);
        assertNull(patrocinioResourceMock);

    }


    /**
     * TEST SET VIP
    */

    @Test
    void setVIPTest() {

        when(repoColaboradorMock.findOne(idMock)).thenReturn(colabMock);
        colabMock.setVip(true);
        colabMock.setMoney(colabMock.getMoney() + moneyMock);
        repoColaboradorMock.save(colabMock);

    }

    /**
     * TEST MODIFY
     */

    @Mock
    Patrocinio patrocinioMock;
    LocalDate dateMock;
    Integer typeMock;

    @Test
    void modify() {

        when(repoPatrocinioMock.findById(idMock)).thenReturn(patrocinioMock);
        when(patrocinioMock.getEndtime().toLocalDate()).thenReturn(dateMock);

        switch(typeMock){
            case 1: // 1 mes
                patrocinioMock.setEndtime(Date.valueOf(dateMock.plusDays(30)));
                break;
            case 2: // 3 meses
                patrocinioMock.setEndtime(Date.valueOf(dateMock.plusDays(30*3)));
                break;
            case 3: // 6 meses
                patrocinioMock.setEndtime(Date.valueOf(dateMock.plusDays(30*6)));
                break;
            case 4: // 12 meses
                patrocinioMock.setEndtime(Date.valueOf(dateMock.plusDays(30*12)));
                break;

        }


        when(patrocinioConverterMock.convert(patrocinioMock)).thenReturn(patrocinioResourceMock);
        assertNotNull(patrocinioResourceMock);
    }
}