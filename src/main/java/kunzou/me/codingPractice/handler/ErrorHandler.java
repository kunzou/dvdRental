package kunzou.me.codingPractice.handler;

import kunzou.me.codingPractice.dto.ErrorResponse;
import kunzou.me.codingPractice.exception.CustomerNotFoundException;
import kunzou.me.codingPractice.exception.FilmNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice(annotations = RestController.class)
public class ErrorHandler extends ResponseEntityExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleInternalError(Exception ex) {
    logger.error(ex.getMessage(), ex);
    ErrorResponse errors = new ErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setError("Unexpected error occurred. Please contact administrator if this issue persists");
    errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({FilmNotFoundException.class, CustomerNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(Exception ex) {

    ErrorResponse errors = new ErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setError(ex.getMessage());
    errors.setStatus(HttpStatus.BAD_REQUEST.value());

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
