package main.persistence.repository;

import main.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    User getByName(String name);

    List<User> findBynameContainingIgnoreCase(String name);

    @Query(value = "select * from user join numvalpubli on userId = user order by numpubli desc limit 15", nativeQuery = true)
    List<User> findByPopuPubli();

    @Query(value = "select * from user join numvalpubli on userId = user order by numval desc limit 15", nativeQuery = true)
    List<User> findByPopuVal();


    // Devuelve una lista de usuarios que siguen tus follows.
    @Query (value="select u1.* " +
            "from follow a1 join user u1 on a1.followed = u1.userId " +
            "where not exists (select * " +
            "                  from follow " +
            "                  where follower = ?1 and followed = a1.followed) " +
            "      and exists (select * " +
            "                  from follow a2 " +
            "                  where a2.follower = ?1 " +
            "                  and exists (select * " +
            "                              from follow a3 " +
            "                              where a3.follower = a2.followed and a3.followed = a1.followed)) " +
            "group by u1.userId, u1.name " +
            "order by count(u1.userId) "  +
            "limit 50", nativeQuery = true)
    List<User> findFollowedByFriends(Integer userID);


    @Query(value="select u2.* " +
            "from user u1 join follow a1 on u1.userId = a1.follower " +
            "                join user u2 on u2.userId = a1.followed " +

            "where u2.userId != ?1 " +
            "and exists (select * " +
            "            from follow a2 " +
            "            where a2.follower = u1.userId and a2.followed = ?1) "+

            "group by u2.userId, u2.name " +
            "order by count(u2.userId) desc " +
            "limit 10",

            nativeQuery = true)
    List<User> usersFollowedByUsersWhoFollow(Integer userID);


}

