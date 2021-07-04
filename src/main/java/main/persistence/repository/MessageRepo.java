package main.persistence.repository;

import main.persistence.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepo extends JpaRepository<Message,Integer>  {
    List<Message> findBySenderOrReceiver(Integer iduser1, Integer iduser2);

}
