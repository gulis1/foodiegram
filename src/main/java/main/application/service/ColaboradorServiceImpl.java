package main.application.service;


import main.domain.converter.RestaurantConverter;
import main.domain.resource.RestaurantResource;
import main.persistence.entity.Restaurant;
import main.persistence.entity.RoleEnum;
import main.persistence.entity.User;
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.UserRepo;
import main.rest.forms.CollaborateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ColaboradorServiceImpl implements ColaboradorService {

    private RestaurantConverter converterCol = new RestaurantConverter();

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestaurantRepo repoColaborador;

    @Autowired
    private RestService restService;

    @Override
    public RestaurantResource upgradeUser(Integer userid, CollaborateForm form) {

        String country=null;
        String city=null;
        String street= null;

        User user = userRepo.getById(userid);

        if (form.getLatitud() != null && form.getLongitud() != null) {
            Map<String, Object> geoData = restService.getGeoData(form.getLatitud(), form.getLongitud());
            try {
                country = geoData.get("country").toString();
                city = geoData.get("city").toString();
                street=geoData.get("street").toString();
            }

            catch (NullPointerException ignored) {

            }
        }

        Restaurant colaborador = new Restaurant(form.getOrigin(), form.getType(), country, city, street, user);
        user.setRole(RoleEnum.ROLE_COL);
        userRepo.save(user);
        repoColaborador.save(colaborador);

        return converterCol.convert(Optional.of(colaborador));
    }

    @Override
    public RestaurantResource getCollab(Integer userID) {
        return converterCol.convert(repoColaborador.findByOwner_Userid(userID));
    }


}
