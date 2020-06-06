package kunzou.me.codingPractice.service;

import kunzou.me.codingPractice.domain.Customer;
import kunzou.me.codingPractice.dto.AvailableFilm;
import kunzou.me.codingPractice.dto.CustomerInformation;
import kunzou.me.codingPractice.dto.FilmInformation;

import java.util.List;

public interface RentalService {
  List<Customer> getAllCustomers();
  List<AvailableFilm> getAvailableFilms();
  FilmInformation getFilmInformation(Long id);
  CustomerInformation getCustomerInformation(Long id);
}
