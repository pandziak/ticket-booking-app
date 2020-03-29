package pl.pandziak.ticketbookingapp.domain.reservation;

import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationRequestDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationResponseDto;

public interface ReservationFacade {

    ReservationResponseDto getReservation(String reservationId);

    ReservationResponseDto createReservation(ReservationRequestDto reservationRequest);

    void cancelReservation(String reservationId);

}
