package pl.pandziak.ticketbookingapp.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pandziak.ticketbookingapp.domain.reservation.ReservationFacade;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationRequestDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationResponseDto;

@RestController
@RequestMapping("/reservations")
class ReservationEndpoint {

    private final ReservationFacade reservationFacade;

    ReservationEndpoint(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @GetMapping("/{id}")
    ReservationResponseDto getReservation(@PathVariable String id) {
        return reservationFacade.getReservation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ReservationResponseDto createReservation(@RequestBody ReservationRequestDto reservationRequest) {
        return reservationFacade.createReservation(reservationRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancelReservation(@PathVariable String id) {
        reservationFacade.cancelReservation(id);
    }

}
