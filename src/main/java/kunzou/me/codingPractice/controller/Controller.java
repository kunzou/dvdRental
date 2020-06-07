package kunzou.me.codingPractice.controller;

import kunzou.me.codingPractice.domain.Customer;
import kunzou.me.codingPractice.dto.CustomerInformation;
import kunzou.me.codingPractice.dto.FilmInformation;
import kunzou.me.codingPractice.service.CachingService;
import kunzou.me.codingPractice.service.RentalService;
import kunzou.me.codingPractice.dto.AvailableFilm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

  private RentalService rentalService;
  private CachingService cachingService;

  @Autowired
  public void setRentalService(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @Autowired
  public void setCachingService(CachingService cachingService) {
    this.cachingService = cachingService;
  }

  @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Customer>> getCustomers() {
    return ResponseEntity.ok().body(rentalService.getAllCustomers());
  }

  @GetMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerInformation> getCustomerById(@PathVariable("id") Long id) {
    return ResponseEntity.ok().body(rentalService.getCustomerInformation(id));
  }

  @GetMapping(value = "/availableFilms", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AvailableFilm>> getAvailableFilms() {
    return ResponseEntity.ok().body(rentalService.getAvailableFilms());
  }

  @GetMapping(value = "/film/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FilmInformation> getFilmById(@PathVariable("id") Long id) {
    return ResponseEntity.ok().body(rentalService.getFilmInformation(id));
  }

  @GetMapping("/cache/reset/all")
  public ResponseEntity resetAllCache() {
    cachingService.clearAllCache();
    return ResponseEntity.ok().build();
  }

  @GetMapping("/cache/reset/customer/{id}")
  public ResponseEntity resetCustomerCache(@PathVariable("id") Long id) {
    cachingService.removeCustomer(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/cache/reset/film/{id}")
  public ResponseEntity resetFilmCache(@PathVariable("id") Long id) {
    cachingService.removeFilm(id);
    return ResponseEntity.ok().build();
  }
}
