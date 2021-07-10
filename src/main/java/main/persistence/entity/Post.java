package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.security.ForbiddenException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Data
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer postid;
    private String title;
    private String text;
    private String image;
    private String country;
    private String city;
    private  Float avg;
    private Integer numratings;
    private Date day;

    public Post(String title, String text, User user, String image, String pais, String ciudad) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.user = user;
        this.country = pais;
        this.city = ciudad;
        this.avg = 0f;
        this.numratings = 0;
        this.day = new Date(Calendar.getInstance().getTime().getTime());
    }

    public Post(String title, String text, User user, String pais, String ciudad) {
       this(title, text, user, null, pais, ciudad);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;

    @PreRemove
    @PreUpdate
    private void preventUnauthorizedRemove() throws ForbiddenException {

        Integer deleterId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (!deleterId.equals(user) && !authorities.contains(RoleEnum.ROLE_MOD) && !authorities.contains(RoleEnum.ROLE_ADMIN))
            throw new ForbiddenException("You're not allowed to do that");
    }

}
