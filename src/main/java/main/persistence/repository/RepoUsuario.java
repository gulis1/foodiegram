package main.persistence.repository;

import main.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByName(String name);
    Usuario getByName(String name);
    List<Usuario> findBynameContainingIgnoreCase(String name);

    @Query(value = "select * from usuario join numvalpubli on id = iduser order by numpubli desc limit 15", nativeQuery = true)
    List<Usuario> findByPopuPubli();

    @Query(value = "select * from usuario join numvalpubli on id = iduser order by numval desc limit 15", nativeQuery = true)
    List<Usuario> findByPopuVal();


    // Devuelve una lista de usuarios que siguen tus amigos.
    @Query (value="select u1.* " +
            "from amigo a1 join usuario u1 on a1.iduser2 = u1.id " +
            "where not exists (select * " +
            "                  from amigo " +
            "                  where iduser1 = ?1 and iduser2 = a1.iduser2) " +
            "      and exists (select * " +
            "                  from amigo a2 " +
            "                  where a2.iduser1 = ?1 " +
            "                  and exists (select * " +
            "                              from amigo a3 " +
            "                              where a3.iduser1 = a2.iduser2 and a3.iduser2 = a1.iduser2)) " +
            "group by u1.id, u1.name " +
            "order by count(u1.id) "  +
            "limit 50", nativeQuery = true)
    List<Usuario> findFollowedByFriends(Integer userID);


    @Query(value="select u2.* " +
            "from usuario u1 join amigo a1 on u1.id = a1.iduser1 " +
            "                join usuario u2 on u2.id = a1.iduser2 " +

            "where u2.id != ?1 " +
            "and exists (select * " +
            "            from amigo a2 " +
            "            where a2.iduser1 = u1.id and a2.iduser2 = ?1) "+

            "group by u2.id, u2.name " +
            "order by count(u2.id) desc " +
            "limit 10",

            nativeQuery = true)
    List<Usuario> usersFollowedByUsersWhoFollow(Integer userID);


}

