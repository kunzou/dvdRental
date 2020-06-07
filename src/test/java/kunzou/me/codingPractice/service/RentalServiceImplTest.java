package kunzou.me.codingPractice.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import kunzou.me.codingPractice.domain.Customer;
import kunzou.me.codingPractice.dto.AvailableFilm;

import kunzou.me.codingPractice.dto.CustomerInformation;
import kunzou.me.codingPractice.dto.FilmInformation;
import kunzou.me.codingPractice.exception.CustomerNotFoundException;
import kunzou.me.codingPractice.exception.FilmNotFoundException;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mongodb.client.model.Filters.lt;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RentalServiceImplTest {

  private RentalServiceImpl rentalService;
  MongoClient mongoClient;
  MongoCollection<Document> customerCollection;
  MongoCollection<Document> filmCollection;

  @Autowired private MongoTemplate mongoTemplate;

  @Before
  public void setup() {
    setupEnvironment();
    setupData();
    setupVariables();
  }

  @After
  public void tearDown() {
    customerCollection.deleteMany(lt("_id", 0));
    filmCollection.deleteMany(lt("_id", 0));
  }

  @Test
  public void getAllCustomers() {
    ReflectionTestUtils.setField(rentalService, "MAXIMUM_RESULTS", 1);
    List<Customer> customers = rentalService.getAllCustomers();
    assertEquals(1, customers.size());
    ReflectionTestUtils.setField(rentalService, "MAXIMUM_RESULTS", 1000);
  }


  @Test
  public void getAvailableFilms() {
    List<AvailableFilm> availableFilms = rentalService.getAvailableFilms();
    assertTrue(availableFilms.stream().noneMatch(film -> film.getId().equals(-1L)));
    assertTrue(availableFilms.stream().anyMatch(film -> film.getId().equals(-2L)));
    assertTrue(availableFilms.stream().noneMatch(film -> film.getId().equals(-3L)));
  }

  @Test
  public void getFilmInformation() {
    FilmInformation filmInformation = rentalService.getFilmInformation(-1L);
    assertEquals(2, filmInformation.getCustomers().size());
    assertTrue(filmInformation.getCustomers().stream().anyMatch(customer -> customer.getId().equals(-1L)));
    assertTrue(filmInformation.getCustomers().stream().anyMatch(customer -> customer.getId().equals(-4L)));
  }

  @Test(expected = FilmNotFoundException.class)
  public void getFilmInformation_exception() {
    rentalService.getFilmInformation(-4L);
  }

  @Test
  public void getCustomerInformation() {
    CustomerInformation customerInformation = rentalService.getCustomerInformation(-1L);
    assertEquals(2, customerInformation.getCustomerRentalList().size());
    assertTrue(customerInformation.getCustomerRentalList().stream().anyMatch(rental -> rental.getFilmTitle().equals("BEDAZZLED MARRIED 1")));
    assertTrue(customerInformation.getCustomerRentalList().stream().anyMatch(rental -> rental.getFilmTitle().equals("BEDAZZLED MARRIED 2")));
  }

  @Test(expected = CustomerNotFoundException.class)
  public void getCustomerInformation_exception() {
    assertNotNull(rentalService.getCustomerInformation(-5L));
  }

  private void setupEnvironment() {
    rentalService = new RentalServiceImpl(mongoTemplate);

    mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("jsonar");
    customerCollection = db.getCollection("customer");
    filmCollection = db.getCollection("film");
  }

  private void setupVariables() {
    ReflectionTestUtils.setField(rentalService, "MAXIMUM_RESULTS", 1000);
    ReflectionTestUtils.setField(rentalService, "FILM_NOT_FOUND", "not found");
    ReflectionTestUtils.setField(rentalService, "CUSTOMER_NOT_FOUND", "not found");
  }

  private void setupData() {
    String futureDateString = LocalDateTime.now().plusDays(5).format(DateTimeFormatter.ofPattern(Customer.DATE_FORMATTER));
    String pastDateString = LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ofPattern(Customer.DATE_FORMATTER));

    String jsonCustomer1 = "{\n" +
      "  \"_id\": -1,\n" +
      "  \"Address\": \"1566 Inegl Manor\",\n" +
      "  \"City\": \"Myingyan\",\n" +
      "  \"Country\": \"Myanmar\",\n" +
      "  \"District\": \"Mandalay\",\n" +
      "  \"First Name\": \"BARBARA\",\n" +
      "  \"Last Name\": \"JONES\",\n" +
      "  \"Phone\": \"705814003527\",\n" +
      "  \"Rentals\": [\n" +
      "    {\n" +
      "      \"Film Title\": \"BEDAZZLED MARRIED 1\",\n" +
      "      \"Payments\": [\n" +
      "        {\n" +
      "          \"Amount\": 0.9900000095367432,\n" +
      "          \"Payment Date\": \"2005-06-16 08:08:40.0\",\n" +
      "          \"Payment Id\": 87\n" +
      "        }\n" +
      "      ],\n" +
      "      \"Rental Date\": \"2005-06-16 08:08:40.0\",\n" +
      "      \"Return Date\": \"" + futureDateString + "\",\n" +
      "      \"filmId\": -1,\n" +
      "      \"rentalId\": 1633,\n" +
      "      \"staffId\": 1\n" +
      "    }\n" +
      "    {\n" +
      "      \"Film Title\": \"BEDAZZLED MARRIED 2\",\n" +
      "      \"Payments\": [\n" +
      "        {\n" +
      "          \"Amount\": 0.9900000095367432,\n" +
      "          \"Payment Date\": \"2005-06-16 08:08:40.0\",\n" +
      "          \"Payment Id\": 87\n" +
      "        }\n" +
      "      ],\n" +
      "      \"Rental Date\": \"2005-06-16 08:08:40.0\",\n" +
      "      \"Return Date\": \"2005-06-17 11:12:40.0\",\n" +
      "      \"filmId\": -2,\n" +
      "      \"rentalId\": 1633,\n" +
      "      \"staffId\": 1\n" +
      "    }\n" +
      "  ]\n" +
      "}";

    String jsonCustomer2 = "{\n" +
      "  \"_id\": -2,\n" +
      "  \"Address\": \"1566 Inegl Manor\",\n" +
      "  \"City\": \"Myingyan\",\n" +
      "  \"Country\": \"Myanmar\",\n" +
      "  \"District\": \"Mandalay\",\n" +
      "  \"First Name\": \"BARBARA\",\n" +
      "  \"Last Name\": \"JONES\",\n" +
      "  \"Phone\": \"705814003527\",\n" +
      "  \"Rentals\": [\n" +
      "    {\n" +
      "      \"Film Title\": \"BEDAZZLED MARRIED 2\",\n" +
      "      \"Payments\": [\n" +
      "        {\n" +
      "          \"Amount\": 0.9900000095367432,\n" +
      "          \"Payment Date\": \"2005-06-16 08:08:40.0\",\n" +
      "          \"Payment Id\": 87\n" +
      "        }\n" +
      "      ],\n" +
      "      \"Rental Date\": \"2005-06-16 08:08:40.0\",\n" +
      "      \"Return Date\": \"" + pastDateString + "\",\n" +
      "      \"filmId\": -2,\n" +
      "      \"rentalId\": 1633,\n" +
      "      \"staffId\": 1\n" +
      "    }\n" +
      "  ]\n" +
      "}";

    String jsonCustomer3 = "{\n" +
      "  \"_id\": -3,\n" +
      "  \"Address\": \"1566 Inegl Manor\",\n" +
      "  \"City\": \"Myingyan\",\n" +
      "  \"Country\": \"Myanmar\",\n" +
      "  \"District\": \"Mandalay\",\n" +
      "  \"First Name\": \"BARBARA\",\n" +
      "  \"Last Name\": \"JONES\",\n" +
      "  \"Phone\": \"705814003527\",\n" +
      "  \"Rentals\": [\n" +
      "    {\n" +
      "      \"Film Title\": \"BEDAZZLED MARRIED\",\n" +
      "      \"Payments\": [\n" +
      "        {\n" +
      "          \"Amount\": 0.9900000095367432,\n" +
      "          \"Payment Date\": \"2005-06-16 08:08:40.0\",\n" +
      "          \"Payment Id\": 87\n" +
      "        }\n" +
      "      ],\n" +
      "      \"Rental Date\": \"2005-06-16 08:08:40.0\",\n" +
      "      \"Return Date\": null,\n" +
      "      \"filmId\": -3,\n" +
      "      \"rentalId\": 1633,\n" +
      "      \"staffId\": 1\n" +
      "    }\n" +
      "  ]\n" +
      "}";

    String jsonCustomer4 = "{\n" +
      "  \"_id\": -4,\n" +
      "  \"Address\": \"1566 Inegl Manor\",\n" +
      "  \"City\": \"Myingyan\",\n" +
      "  \"Country\": \"Myanmar\",\n" +
      "  \"District\": \"Mandalay\",\n" +
      "  \"First Name\": \"BARBARA\",\n" +
      "  \"Last Name\": \"JONES\",\n" +
      "  \"Phone\": \"705814003527\",\n" +
      "  \"Rentals\": [\n" +
      "    {\n" +
      "      \"Film Title\": \"BEDAZZLED MARRIED\",\n" +
      "      \"Payments\": [\n" +
      "        {\n" +
      "          \"Amount\": 0.9900000095367432,\n" +
      "          \"Payment Date\": \"2005-06-16 08:08:40.0\",\n" +
      "          \"Payment Id\": 87\n" +
      "        }\n" +
      "      ],\n" +
      "      \"Rental Date\": \"2005-06-16 08:08:40.0\",\n" +
      "      \"Return Date\": \"2005-06-17 08:08:40.0\",\n" +
      "      \"filmId\": -1,\n" +
      "      \"rentalId\": 1633,\n" +
      "      \"staffId\": 1\n" +
      "    }\n" +
      "  ]\n" +
      "}";

    String jsonFilm1 = "{\n" +
      "  \"_id\": -1,\n" +
      "  \"Actors\": [\n" +
      "    {\n" +
      "      \"First name\": \"CARMEN\",\n" +
      "      \"Last name\": \"HUNT\",\n" +
      "      \"actorId\": 52\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"WALTER\",\n" +
      "      \"Last name\": \"TORN\",\n" +
      "      \"actorId\": 102\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"ED\",\n" +
      "      \"Last name\": \"MANSFIELD\",\n" +
      "      \"actorId\": 136\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"EWAN\",\n" +
      "      \"Last name\": \"GOODING\",\n" +
      "      \"actorId\": 139\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"IAN\",\n" +
      "      \"Last name\": \"TANDY\",\n" +
      "      \"actorId\": 155\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"LAURA\",\n" +
      "      \"Last name\": \"BRODY\",\n" +
      "      \"actorId\": 159\n" +
      "    }\n" +
      "  ],\n" +
      "  \"Category\": \"Music\",\n" +
      "  \"Description\": \"A Boring Drama of a Woman And a Squirrel who must Conquer a Student in A Baloon\",\n" +
      "  \"Length\": \"79\",\n" +
      "  \"Rating\": \"R\",\n" +
      "  \"Rental Duration\": \"4\",\n" +
      "  \"Replacement Cost\": \"23.99\",\n" +
      "  \"Special Features\": \"Commentaries,Deleted Scenes,Behind the Scenes\",\n" +
      "  \"Title\": \"AMELIE HELLFIGHTERS\"\n" +
      "}";

    String jsonFilm2 = "{\n" +
      "  \"_id\": -2,\n" +
      "  \"Actors\": [\n" +
      "    {\n" +
      "      \"First name\": \"CARMEN\",\n" +
      "      \"Last name\": \"HUNT\",\n" +
      "      \"actorId\": 52\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"WALTER\",\n" +
      "      \"Last name\": \"TORN\",\n" +
      "      \"actorId\": 102\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"ED\",\n" +
      "      \"Last name\": \"MANSFIELD\",\n" +
      "      \"actorId\": 136\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"EWAN\",\n" +
      "      \"Last name\": \"GOODING\",\n" +
      "      \"actorId\": 139\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"IAN\",\n" +
      "      \"Last name\": \"TANDY\",\n" +
      "      \"actorId\": 155\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"LAURA\",\n" +
      "      \"Last name\": \"BRODY\",\n" +
      "      \"actorId\": 159\n" +
      "    }\n" +
      "  ],\n" +
      "  \"Category\": \"Music\",\n" +
      "  \"Description\": \"A Boring Drama of a Woman And a Squirrel who must Conquer a Student in A Baloon\",\n" +
      "  \"Length\": \"79\",\n" +
      "  \"Rating\": \"R\",\n" +
      "  \"Rental Duration\": \"4\",\n" +
      "  \"Replacement Cost\": \"23.99\",\n" +
      "  \"Special Features\": \"Commentaries,Deleted Scenes,Behind the Scenes\",\n" +
      "  \"Title\": \"AMELIE HELLFIGHTERS\"\n" +
      "}";

    String jsonFilm3 = "{\n" +
      "  \"_id\": -3,\n" +
      "  \"Actors\": [\n" +
      "    {\n" +
      "      \"First name\": \"CARMEN\",\n" +
      "      \"Last name\": \"HUNT\",\n" +
      "      \"actorId\": 52\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"WALTER\",\n" +
      "      \"Last name\": \"TORN\",\n" +
      "      \"actorId\": 102\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"ED\",\n" +
      "      \"Last name\": \"MANSFIELD\",\n" +
      "      \"actorId\": 136\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"EWAN\",\n" +
      "      \"Last name\": \"GOODING\",\n" +
      "      \"actorId\": 139\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"IAN\",\n" +
      "      \"Last name\": \"TANDY\",\n" +
      "      \"actorId\": 155\n" +
      "    },\n" +
      "    {\n" +
      "      \"First name\": \"LAURA\",\n" +
      "      \"Last name\": \"BRODY\",\n" +
      "      \"actorId\": 159\n" +
      "    }\n" +
      "  ],\n" +
      "  \"Category\": \"Music\",\n" +
      "  \"Description\": \"A Boring Drama of a Woman And a Squirrel who must Conquer a Student in A Baloon\",\n" +
      "  \"Length\": \"79\",\n" +
      "  \"Rating\": \"R\",\n" +
      "  \"Rental Duration\": \"4\",\n" +
      "  \"Replacement Cost\": \"23.99\",\n" +
      "  \"Special Features\": \"Commentaries,Deleted Scenes,Behind the Scenes\",\n" +
      "  \"Title\": \"AMELIE HELLFIGHTERS\"\n" +
      "}";

    customerCollection.insertOne(Document.parse(jsonCustomer1));
    customerCollection.insertOne(Document.parse(jsonCustomer2));
    customerCollection.insertOne(Document.parse(jsonCustomer3));
    customerCollection.insertOne(Document.parse(jsonCustomer4));
    filmCollection.insertOne(Document.parse(jsonFilm1));
    filmCollection.insertOne(Document.parse(jsonFilm2));
    filmCollection.insertOne(Document.parse(jsonFilm3));
  }
}
