package main.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.persistence.IDs.RatingID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(RatingID.class)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Rating {

  @Id
  private Integer post;
  @Id
  private Integer user;

    private Integer score;

}
