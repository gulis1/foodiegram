package main.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.format.number.money.MonetaryAmountFormatter;

@Entity
@Data
public class Patrocinio {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private java.sql.Date endtime;
    private float money;

    public Patrocinio() {
        this.money = 0.0f;
    }

    public Patrocinio(Colaborador collab, java.sql.Date endtime, Float money) {
        this();
        this.endtime = endtime;
        this.collab = collab;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Colaborador collab;

}