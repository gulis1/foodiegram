package main.persistence.repository;

import main.persistence.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SponsorRepo extends JpaRepository<Sponsor, Integer> {

    Optional<Sponsor> findByRestaurant_restaurantid(int collabID);

}
