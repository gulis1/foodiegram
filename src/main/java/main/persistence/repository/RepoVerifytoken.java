package main.persistence.repository;


import main.persistence.entity.Verifytoken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepoVerifytoken extends JpaRepository<Verifytoken, Integer> {

    Optional<Verifytoken> findByToken(Integer token);

}
