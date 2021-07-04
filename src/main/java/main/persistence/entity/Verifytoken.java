package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
public class Verifytoken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer verifytokenid;
    private String email;
    private Integer token;
    private Date expiredate;

    public Verifytoken(String email, Integer securitytoken){
        this.email=email;
        this.token=securitytoken;
        this.expiredate=calculateExpiryDate();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        System.out.println(cal.getTime().getTime());
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, 20);
        return new Date(cal.getTime().getTime());
    }

}
