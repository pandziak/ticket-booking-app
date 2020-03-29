package pl.pandziak.ticketbookingapp.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pandziak.ticketbookingapp.domain.reservation.exception.ReservationNotFoundException;
import pl.pandziak.ticketbookingapp.domain.screening.exceptions.ScreeningNotFoundException;

@ControllerAdvice
class ApiErrorHandler {

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity handleReservationNotFoundException(ReservationNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex);
    }

    @ExceptionHandler(ScreeningNotFoundException.class)
    public ResponseEntity handleScreeningNotFoundException(ScreeningNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ex);
    }

}
