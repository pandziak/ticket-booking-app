package pl.pandziak.ticketbookingapp.domain.reservation;

import org.springframework.stereotype.Service;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationRequestDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationResponseDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketsDto;
import pl.pandziak.ticketbookingapp.domain.reservation.exception.ReservationNotFoundException;
import pl.pandziak.ticketbookingapp.domain.screening.ScreeningFacade;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningDetailsDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto;

import java.time.temporal.ChronoUnit;

import static java.util.stream.Collectors.toList;

@Service
final class ReservationFacadeImpl implements ReservationFacade {

    private final ScreeningFacade screeningFacade;
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;

    ReservationFacadeImpl(ScreeningFacade screeningFacade, ReservationRepository reservationRepository) {
        this.screeningFacade = screeningFacade;
        this.reservationRepository = reservationRepository;
        this.reservationValidator = new ReservationValidator();
    }

    @Override
    public ReservationResponseDto getReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(reservationId));

        ScreeningResponseDto screeningResponse = screeningFacade.getScreening(reservation.getScreeningId());
        return toReservationResponse(reservation, screeningResponse.getScreeningDetails());
    }

    @Override
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequest) {
        validateReservationOnRequestLevel(reservationRequest);

        ScreeningResponseDto screeningResponse = screeningFacade.getScreening(reservationRequest.getScreeningId());

        validateReservationOnDomainLevel(reservationRequest, screeningResponse);

        Reservation reservation = reservationRequest.toReservation();
        screeningFacade.bookScreeningSeats(reservation.getScreeningId(), mapTickets(reservation));
        reservationRepository.save(reservation);

        return toReservationResponse(reservation, screeningResponse.getScreeningDetails());
    }

    @Override
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        screeningFacade.unbookScreeningSeats(reservation.getScreeningId(), mapTickets(reservation));
        reservationRepository.delete(reservation);
    }

    private void validateReservationOnRequestLevel(ReservationRequestDto reservationRequest) {
        reservationValidator.validateInputFieldsNonNullOrEmpty(reservationRequest);
        reservationValidator.validateUserName(reservationRequest.getUserName());
        reservationValidator.validateUserSurname(reservationRequest.getUserSurname());
        reservationValidator.validateTickets(reservationRequest.getTickets());
    }

    private void validateReservationOnDomainLevel(ReservationRequestDto reservationRequest, ScreeningResponseDto screeningResponse) {
        reservationValidator.validateTime(screeningResponse.getScreeningDetails().getScreeningTime());
        reservationValidator.validateChosenSeats(reservationRequest.getTickets(), screeningResponse);
    }

    private ReservationResponseDto toReservationResponse(Reservation reservation, ScreeningDetailsDto screening) {
        return new ReservationResponseDto.Builder()
                .withReservationId(reservation.getId())
                .withScreeningDetails(screening)
                .withTickets(mapTickets(reservation))
                .withTotalPrice(reservation.getTickets().calculateTotalPrice())
                .withExpirationAt(reservation.getCreatedAt().plus(30, ChronoUnit.MINUTES))
                .build();
    }

    private TicketsDto mapTickets(Reservation reservation) {
        return new TicketsDto(
                reservation.getTickets()
                        .getTickets()
                        .stream()
                        .map(t -> new TicketDto(t.getRow(), t.getColumn(), t.getTicketPricing().name()))
                        .collect(toList()));
    }

}