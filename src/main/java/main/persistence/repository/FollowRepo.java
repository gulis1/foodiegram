package main.persistence.repository;

import main.persistence.IDs.FollowID;
import main.persistence.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepo extends JpaRepository<Follow, FollowID> {

}
