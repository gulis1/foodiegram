package main.application.service;

import main.domain.resource.SponsorResource;

public interface SponsorService {




    // devuelve un patrocinio existente o null en caso contrario
    SponsorResource getSponsorship(Integer id);
    // CREA/MODIFICA UN PATROCINIO
    //
    // crea un nuevo patrocinio
    // devuelve un patrocinio creado
    SponsorResource obtain(Integer id, Integer type, Float money);
    // MODIFICAR PATROCINIO
    //
    // amplia el tiempo de patrocinio
    // devuelve un patrocinio modificado
    SponsorResource modify(Integer id, Integer type, Float money);

}
