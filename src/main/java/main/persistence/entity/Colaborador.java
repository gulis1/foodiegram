package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyToOne;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Colaborador {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String origin;
    private String type;
    private String pais;
    private String ciudad;
    private String calle;
    private Boolean vip;

    public Colaborador(String origin, String type, String pais, String ciudad, String calle, Usuario owner) {
        this.owner = owner;
        this.origin = origin;
        this.type = type;
        this.calle=calle;
        this.ciudad=ciudad;
        this.pais=pais;
        vip=false;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Usuario owner;
}
