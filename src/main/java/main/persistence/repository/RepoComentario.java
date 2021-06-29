package main.persistence.repository;

import main.persistence.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoComentario extends JpaRepository<Comentario, Integer> {

    List<Comentario> findByIdpubliOrderByIdAsc(Integer idpubli);
}
