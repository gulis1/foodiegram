package main.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.persistence.IDs.FollowID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@IdClass(FollowID.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="follow")
public class Follow {

    @Id
    private Integer follower;
    @Id
    private Integer followed;
}
