package pl.pandziak.ticketbookingapp.domain.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String reservationId) {
        super("Could not find reservation with id: " + reservationId);
    }

}
