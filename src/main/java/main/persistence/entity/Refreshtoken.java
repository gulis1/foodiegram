package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
public class Refreshtoken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer refreshtokenid;
    private Integer user;
    private Date expiredate;

    public Refreshtoken(Integer user, Date expiredate) {
        this.user = user;
        this.expiredate = expiredate;
    }

}
