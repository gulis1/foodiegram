package main.persistence.IDs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FollowID implements Serializable {

    private Integer follower;
    private Integer followed;

    protected FollowID(){}



}
