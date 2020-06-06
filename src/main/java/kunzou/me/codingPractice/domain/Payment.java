
package kunzou.me.codingPractice.domain;

import javax.annotation.Generated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Payment {

    @Field("Amount")
    private Double amount;
    @Field("Payment Date")
    private LocalDateTime paymentDate;
    @Field("Payment Id")
    private Long paymentId;

}
