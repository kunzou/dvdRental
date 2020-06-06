
package kunzou.me.codingPractice.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Film {

    @Field("_id")
    @Id
    private Long id;
    @Field("Actors")
    private List<Actor> actors;
    @Field("Category")
    private String category;
    @Field("Description")
    private String description;
    @Field("Length")
    private Integer length;
    @Field("Rating")
    private String rating;
    @Field("Rental Duration")
    private Integer rentalDuration;
    @Field("Replacement Cost")
    private Double replacementCost;
    @Field("Special Features")
    private String specialFeatures;
    @Field("Title")
    private String title;

  public List<Actor> getActors() {
    if(actors == null) {
      actors = new ArrayList<>();
    }
    return actors;
  }
}
