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
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer messageid;
    private Integer sender;
    private Integer receiver;
    private String text;

    public Message(Integer sender, Integer receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    @PreRemove
    private void preventUnauthorizedRemove() throws ForbiddenException {

        Integer deleterId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (!deleterId.equals(sender) && !authorities.contains(RoleEnum.ROLE_MOD) && !authorities.contains(RoleEnum.ROLE_ADMIN))
            throw new ForbiddenException("You're not allowed to do that");
    }

}
