package main.persistence.repository;

import main.persistence.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostOrderByCommentidAsc(Integer idpubli);

}
