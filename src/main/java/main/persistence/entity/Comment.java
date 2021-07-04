package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.security.ForbiddenException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer commentid;
    private Integer post;
    private String text;

    public Comment(Integer idPubli, User usuario, String text) {
        this.post = idPubli;
        this.autor=usuario;
        this.text = text;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User autor;

    @PreRemove
    @PreUpdate
    private void preventUnauthorizedRemove() throws ForbiddenException {

        Integer deleterId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (!deleterId.equals(this.getAutor().getUserid()) && !authorities.contains(RoleEnum.ROLE_MOD) && !authorities.contains(RoleEnum.ROLE_ADMIN))
            throw new ForbiddenException("You're not allowed to do that");
    }

}
