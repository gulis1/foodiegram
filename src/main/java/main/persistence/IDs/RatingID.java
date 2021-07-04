package main.persistence.IDs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RatingID implements Serializable {

    private Integer post;
    private Integer user;

    protected RatingID() {}

}
