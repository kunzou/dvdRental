package kunzou.me.codingPractice.dto;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AvailableFilm {
  @Field("_id")
  private Long id;
  @Field("Title")
  private String title;
  @Field("Category")
  private String category;
  @Field("Description")
  private String description;
  @Field("Rating")
  private String rating;
  @Field("Rental Duration")
  private Integer rentalDuration;
}
