package main.application.service;

import main.domain.resource.RestaurantResource;
import main.rest.forms.CollaborateForm;

public interface ColaboradorService {

     //Se registra un colaborador a partir de un usuario ya registrado ( se le cambia los roles )
    public RestaurantResource upgradeUser(Integer uder, CollaborateForm form);

    public RestaurantResource getCollab(Integer userID);
}
