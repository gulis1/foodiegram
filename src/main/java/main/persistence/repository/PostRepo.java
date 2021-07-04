package main.persistence.repository;

import main.persistence.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

     List<Post> findByUser_useridOrderByPostidDesc(Integer id);

     @Query (value="SELECT * FROM post "+
             "where ( ?1 = -1 or post.day > date_sub(NOW(), interval ?1 DAY) ) and (IFNULL(post.city, 'xxx') LIKE ?3 and IFNULL(post.contry LIKE ?2, 'xxx'))" +
             "ORDER BY post.avg DESC "+
             "LIMIT 0,50 "
             ,nativeQuery = true)
     List<Post> bestRated(Integer amount, String country, String city);

     @Query (value="SELECT * FROM post "+
             "where ( ?1 = -1 or post.day > date_sub(NOW(), interval ?1 DAY) ) and (IFNULL(post.city, 'xxx') LIKE ?3 and IFNULL(post.contry LIKE ?2, 'xxx'))" +
             "ORDER BY post.numRatings DESC "+
             "LIMIT 0,50 "

             ,nativeQuery = true)
     List<Post> mostRated(Integer amount, String pais, String ciudad);

     @Query(value="SELECT post.* " +
             "FROM post JOIN follow ON follow.followed = post.user " +
             "WHERE  follow.follower=?1 "+
             "ORDER BY post.day DESC",nativeQuery = true)
     List<Post> fromFriends(Integer user1);

     @Query(value = "SELECT post.* " +
             "FROM post " +
             "post.text LIKE %?1%", nativeQuery = true)
     List<Post> findByTag(String tag);




}