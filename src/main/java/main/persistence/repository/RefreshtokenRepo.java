package main.persistence.repository;

import main.persistence.entity.Refreshtoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshtokenRepo extends JpaRepository<Refreshtoken,Integer> {

    Optional<Refreshtoken> findByUser(Integer userid);

}
