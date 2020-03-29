package pl.pandziak.ticketbookingapp.domain.screening.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateFormatException extends RuntimeException {

    public InvalidDateFormatException(String from) {
        super("Invalid date: " + from + ". Please use yyyy-MM-ddTHH:mm in 'from' parameter");
    }

}
