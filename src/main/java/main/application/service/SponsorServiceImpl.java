package main.application.service;

import main.domain.converter.PatrocinioConverter;
import main.domain.resource.SponsorResource;
import main.persistence.entity.Restaurant;
import main.persistence.entity.Sponsor;
import main.persistence.repository.RestaurantRepo;
import main.persistence.repository.SponsorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class SponsorServiceImpl implements SponsorService {

    private PatrocinioConverter converterSponsor = new PatrocinioConverter();

    @Autowired
    private SponsorRepo repoSponsor;

    @Autowired
    private RestaurantRepo repoColab;

    // devuelve la fecha de vencimiento del patrocinio de un nuevo sponsor
    private String getNewDateNewSponsor(Integer days) {
        LocalDate date = LocalDate.now();
        return date.plusDays(days).toString();
    }

    // devuelve un patrocinio existente o null en caso contrario
    public SponsorResource getSponsorship(Integer id) {
        return converterSponsor.convert(repoSponsor.findByRestaurant_restaurantid(id));
    }


    // pone a true el valor de VIP y mete el dinero aportado
    public void setVIP(Integer id) {

        Optional<Restaurant> colab = repoColab.findById(id);
        
        
        if (colab.isPresent()) {
            colab.get().setVip(true);
            repoColab.save(colab.get());
         }

    }


    // CREA/MODIFICA UN PATROCINIO
    //
    // crea un nuevo patrocinio
    // devuelve un patrocinio creado
    public SponsorResource obtain(Integer userID, Integer type, Float money) {

        Restaurant col = repoColab.getByOwner_Userid(userID);

        Sponsor sponsorship = null;
        
        switch(type) {
            case 1: // 1 mes
                sponsorship = new Sponsor(col, Date.valueOf(getNewDateNewSponsor(30)), money);
                break;
            case 2: // 3 meses
                sponsorship = new Sponsor(col, Date.valueOf(getNewDateNewSponsor(30*3)), money);
                break;
            case 3: // 6 meses
                sponsorship = new Sponsor(col, Date.valueOf(getNewDateNewSponsor(30*6)), money);
                break;
            case 4: // 12 meses
                sponsorship = new Sponsor(col, Date.valueOf(getNewDateNewSponsor(30*12)), money);
                break;
        }

        repoSponsor.save(sponsorship);

        setVIP(col.getRestaurantid());

        return converterSponsor.convert(Optional.of(sponsorship));
    }


    // MODIFICAR PATROCINIO
    //
    // amplia el tiempo de patrocinio
    // devuelve un patrocinio modificado
    public SponsorResource modify(Integer id, Integer type, Float money) {

        Optional<Sponsor> sponsorship = repoSponsor.findById(id);

        if (sponsorship.isPresent()) {

            LocalDate date = sponsorship.get().getEndtime().toLocalDate();
            switch(type) {
                case 1: // 1 mes
                    sponsorship.get().setEndtime(Date.valueOf(date.plusDays(30)));
                    break;
                case 2: // 3 meses
                    sponsorship.get().setEndtime(Date.valueOf(date.plusDays(30*3)));
                    break;
                case 3: // 6 meses
                    sponsorship.get().setEndtime(Date.valueOf(date.plusDays(30*6)));
                    break;
                case 4: // 12 meses
                    sponsorship.get().setEndtime(Date.valueOf(date.plusDays(30*12)));
                    break;
            }

            repoSponsor.save(sponsorship.get());
            setVIP(id);
        }

        return converterSponsor.convert(sponsorship);
    }
}
