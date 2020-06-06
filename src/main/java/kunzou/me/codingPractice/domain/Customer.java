
package kunzou.me.codingPractice.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.Id;

import com.google.gson.annotations.Expose;
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
public class Customer {
  public static final String DATE_FORMATTER= "yyyy-MM-dd HH:mm:ss.S";

  @Id
  @Field("_id")
  private Long id;
  @Field("Address")
  private String address;
  @Field("City")
  private String city;
  @Field("Country")
  private String country;
  @Field("District")
  private String district;
  @Field("First Name")
  private String firstName;
  @Field("Last Name")
  private String lastName;
  @Field("Phone")
  private String phone;
  @Field("Rentals")
  private List<Rental> rentals;

  public List<Rental> getRentals() {
    if(rentals == null) {
      rentals = new ArrayList<>();
    }
    return rentals;
  }
}
