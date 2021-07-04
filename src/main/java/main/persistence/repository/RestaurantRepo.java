package main.persistence.repository;

import main.persistence.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {

    Restaurant getByOwner_Userid(Integer owner);

    Optional<Restaurant> findByOwner_Userid(Integer owner);

    @Query(value = "SELECT * FROM restaurant AS c, (SELECT * FROM user WHERE user.name LIKE %?1%) AS u WHERE c.owner = u.id", nativeQuery = true)
    List<Restaurant> findByUsername(String colabname);
    
    @Query(value = "SELECT * FROM (SELECT * FROM restaurant WHERE restaurant.type LIKE %?1%) AS c, user AS u WHERE c.owner = u.id", nativeQuery = true)
    List<Restaurant> findByType(String type);


    @Query(value="SELECT * FROM restaurant JOIN user ON restaurant.owner = user.userId "+
            "WHERE IFNULL(restaurant.city, 'xxx') LIKE ?2 and IFNULL(restaurant.coutry, 'xxx') LIKE ?1 " +
            "ORDER BY restaurant.vip DESC "+
            "LIMIT 0,50 "
            ,nativeQuery = true)
    List<Restaurant>  descubrirCollab (String country, String city);
}
