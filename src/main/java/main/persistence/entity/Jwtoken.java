package main.persistence.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Jwtoken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer jwtokenid;
    private Integer user;
    private Date expiredate;

    public Jwtoken(Integer user, Date expiredate) {
        this.user = user;
        this.expiredate = expiredate;
    }

}
