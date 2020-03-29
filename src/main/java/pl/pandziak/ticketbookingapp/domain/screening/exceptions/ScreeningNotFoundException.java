package pl.pandziak.ticketbookingapp.domain.screening.exceptions;

public class ScreeningNotFoundException extends RuntimeException {

    public ScreeningNotFoundException(String screeningId) {
        super("Could not find screening with id: " + screeningId);
    }

}
