package main.rest.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingForm {

    Integer pubID;
    Integer userID;
    Integer score;
}
