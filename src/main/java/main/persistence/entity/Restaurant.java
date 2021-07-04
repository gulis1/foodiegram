package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer restaurantid;
    private String type;
    private String country;
    private String city;
    private String street;
    private Boolean vip;

    public Restaurant(String origin, String type, String country, String ciudad, String calle, User owner) {
        this.owner = owner;
        this.type = type;
        this.street =calle;
        this.city =ciudad;
        this.country = country;
        vip=false;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner")
    private User owner;

}
