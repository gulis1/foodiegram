package main.persistence.repository;


import main.persistence.entity.Verifytoken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifytokenRepo extends JpaRepository<Verifytoken, Integer> {

    Optional<Verifytoken> findByToken(Integer token);

}
