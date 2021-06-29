package main.persistence.repository;

import main.persistence.entity.Patrocinio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoPatrocinio extends JpaRepository<Patrocinio, Integer> {

}
