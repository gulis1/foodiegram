package main.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Sponsor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer sponsorid;
    private java.sql.Date endtime;
    private float money;

    public Sponsor() {
        this.money = 0.0f;
    }

    public Sponsor(Restaurant collab, java.sql.Date endtime, Float money) {
        this();
        this.endtime = endtime;
        this.restaurant = collab;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;

}