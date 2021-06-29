package main.persistence.repository;

import main.persistence.entity.Jwtoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoJwtoken extends JpaRepository<Jwtoken, Integer> {

    Optional<Jwtoken> findByUserid(Integer userid);

}
