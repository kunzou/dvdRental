
package kunzou.me.codingPractice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
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
public class Rental {

    @Expose
    private Long filmId;
    @Field("Film Title")
    private String filmTitle;
    @Field("Payments")
    private List<Payment> payments;
    @Field("Rental Date")
    private LocalDateTime rentalDate;
    @Expose
    private Long rentalId;
    @Field("Return Date")
    private LocalDateTime returnDate;
    @Expose
    private Long staffId;

  public List<Payment> getPayments() {
    if(payments == null) {
      payments =  new ArrayList<>();
    }
    return payments;
  }
}
