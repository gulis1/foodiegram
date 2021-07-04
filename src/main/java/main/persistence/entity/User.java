package main.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer userid;
    private String name;
    private String passwd;
    private String email;
    private String image;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public User(Integer id) {
        this.userid = id;
    }

    public User(String name, String passwd, String image, String email) {
        this.name = name;
        this.passwd = passwd;
        this.image = image;
        this.email=email;
        this.enabled=false;
        this.role = null;

    }

}
