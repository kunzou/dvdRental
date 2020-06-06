package kunzou.me.codingPractice.controller;

import kunzou.me.codingPractice.domain.Customer;
import kunzou.me.codingPractice.dto.CustomerInformation;
import kunzou.me.codingPractice.dto.FilmInformation;
import kunzou.me.codingPractice.service.RentalService;
import kunzou.me.codingPractice.dto.AvailableFilm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

  private RentalService rentalService;

  public Controller(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @GetMapping("/customers")
  public ResponseEntity<List<Customer>> getCustomers() {
    return ResponseEntity.ok().body(rentalService.getAllCustomers());
  }

  @GetMapping("/customer/{id}")
  public ResponseEntity<CustomerInformation> getCustomerById(@PathVariable("id") Long id) {
    return ResponseEntity.ok().body(rentalService.getCustomerInformation(id));
  }

  @GetMapping("/availableFilms")
  public ResponseEntity<List<AvailableFilm>> getAvailableFilms() {
    return ResponseEntity.ok().body(rentalService.getAvailableFilms());
  }

  @GetMapping("/film/{id}")
  public ResponseEntity<FilmInformation> getFilmById(@PathVariable("id") Long id) {
    return ResponseEntity.ok().body(rentalService.getFilmInformation(id));
  }
}
