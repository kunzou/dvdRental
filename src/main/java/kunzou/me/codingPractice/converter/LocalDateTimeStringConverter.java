package kunzou.me.codingPractice.converter;

import kunzou.me.codingPractice.domain.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeStringConverter implements Converter<String, LocalDateTime> {
  @Override
  public LocalDateTime convert(String source) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Customer.DATE_FORMATTER);
    return LocalDateTime.parse(source, formatter);
  }
}
