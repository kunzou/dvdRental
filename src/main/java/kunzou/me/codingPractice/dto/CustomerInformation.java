package kunzou.me.codingPractice.dto;

import kunzou.me.codingPractice.domain.Customer;
import kunzou.me.codingPractice.domain.Rental;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CustomerInformation {
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

  private List<CustomerRental> customerRentalList;

  public List<CustomerRental> getCustomerRentalList() {
    if(customerRentalList == null) {
      customerRentalList = new ArrayList<>();
    }
    return customerRentalList;
  }

  public void addRental(CustomerRental customerRental) {
    getCustomerRentalList().add(customerRental);
  }

}
