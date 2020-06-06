package kunzou.me.codingPractice.service;

import kunzou.me.codingPractice.domain.Customer;
import kunzou.me.codingPractice.domain.Film;
import kunzou.me.codingPractice.domain.Payment;
import kunzou.me.codingPractice.domain.Rental;
import kunzou.me.codingPractice.dto.AvailableFilm;
import kunzou.me.codingPractice.dto.CustomerInformation;
import kunzou.me.codingPractice.dto.CustomerRental;
import kunzou.me.codingPractice.dto.FilmInformation;
import kunzou.me.codingPractice.exception.CustomerNotFoundException;
import kunzou.me.codingPractice.exception.FilmNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;

@org.springframework.stereotype.Service
public class RentalServiceImpl implements RentalService {

  @Value("${mongodb.maximum.result}")
  private Integer maximumResult;

  @Value("${exception.customer.not.found}")
  private String customerNotFoundErrorMessage;

  @Value("${exception.film.not.found}")
  private String filmNotFoundErrorMessage;

  private MongoTemplate mongoTemplate;

  public RentalServiceImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<Customer> getAllCustomers() {
    return mongoTemplate.find(new Query().limit(maximumResult), Customer.class);
  }

  @Override
  public List<AvailableFilm> getAvailableFilms() {
    List<Long> unavailableFilmIds = getUnavailableFilmIdsByReturnDate(LocalDateTime.now());

    MatchOperation matchStage = Aggregation.match(Criteria.where("_id").nin(unavailableFilmIds));
    ProjectionOperation projectStage = Aggregation.project("_id", "Title", "Category", "Description", "Rating", "Rental Duration");

    Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage).withOptions(newAggregationOptions().cursorBatchSize(maximumResult).build());

    return mongoTemplate.aggregate(aggregation, "film", AvailableFilm.class).getMappedResults();
  }

  private List<Long> getUnavailableFilmIdsByReturnDate(LocalDateTime localDateTime) {
    String formatDateTime = localDateTime.format(DateTimeFormatter.ofPattern(Customer.DATE_FORMATTER));
    Criteria criteria = new Criteria().orOperator(Criteria.where("Rentals.Return Date").is(null), Criteria.where("Rentals.Return Date").gt(formatDateTime));

    MatchOperation matchStage = Aggregation.match(criteria);
    ProjectionOperation projectStage = Aggregation.project("Rentals.filmId");
    UnwindOperation unwindOperation = Aggregation.unwind("Rentals");

    Aggregation aggregation = Aggregation.newAggregation(unwindOperation, matchStage, projectStage);

    return mongoTemplate.aggregate(aggregation, "customer", Map.class).getMappedResults().stream()
      .map(map -> Long.valueOf((Integer)map.get("filmId"))).collect(Collectors.toList());
  }

  @Override
  public FilmInformation getFilmInformation(Long id) {
    Film film = mongoTemplate.findById(id, Film.class);
    if(film == null) {
      throw new FilmNotFoundException(filmNotFoundErrorMessage, id);
    }

    Query query = new Query();
    query.addCriteria(Criteria.where("Rentals.filmId").is(id));

    List<CustomerInformation> customers = mongoTemplate.find(query, Customer.class).stream()
      .map(this::buildCustomerInformation)
      .collect(Collectors.toList());
    return new FilmInformation(customers, film);
  }

  private CustomerInformation buildCustomerInformation(Customer customer) {
    CustomerInformation customerInformation = new CustomerInformation();

    BeanUtils.copyProperties(customer, customerInformation, "rentals");
    customer.getRentals().stream()
      .map(this::buildCustomerRental)
      .forEach(customerInformation::addRental);

    return customerInformation;
  }

  @Override
  public CustomerInformation getCustomerInformation(Long id) {
    Customer customer = mongoTemplate.findById(id, Customer.class);
    if(customer == null) {
      throw new CustomerNotFoundException(customerNotFoundErrorMessage, id);
    }
    return buildCustomerInformation(customer);
  }

  private CustomerRental buildCustomerRental(Rental rental) {
    Double costOfRental =  rental.getPayments().stream()
      .mapToDouble(Payment::getAmount)
      .sum();

    return new CustomerRental(
      rental.getFilmTitle(),  //todo: This is not on the requirements but I believe this field is essential to user
      rental.getRentalDate(),
      Math.toIntExact(Duration.between(rental.getRentalDate(), rental.getReturnDate()).toDays()),
      costOfRental);
  }
}
