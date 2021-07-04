package main.persistence.repository;

import main.persistence.IDs.RatingID;
import main.persistence.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RatingRepo extends JpaRepository<Rating, RatingID>{

   List<Rating> findByPost(Integer idpubli);
   List<Rating> findByUser(Integer iduser);
}
