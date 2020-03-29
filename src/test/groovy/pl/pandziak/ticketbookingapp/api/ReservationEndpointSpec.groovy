package pl.pandziak.ticketbookingapp.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.pandziak.ticketbookingapp.ErrorResponse
import pl.pandziak.ticketbookingapp.TicketBookingApplicationSpec
import pl.pandziak.ticketbookingapp.domain.Room
import pl.pandziak.ticketbookingapp.domain.TicketPricing
import pl.pandziak.ticketbookingapp.domain.reservation.ReservationFacade
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationRequestDto
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationResponseDto
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketDto
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketsDto
import pl.pandziak.ticketbookingapp.domain.screening.Screening
import pl.pandziak.ticketbookingapp.domain.screening.ScreeningFacade
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

class ReservationEndpointSpec extends TicketBookingApplicationSpec {

    @Autowired
    private ReservationFacade reservationFacade

    @Autowired
    private ScreeningFacade screeningFacade

    def setup() {
        reservationRepository.deleteAll()
    }

    def "should create reservation"() {
        given:
            Screening screening = createScreening()
            ReservationRequestDto reservationRequest = prepareReservationRequest(screening.getId())

        and:
            URI uri = getBaseUri()
                        .path("reservations")
                        .build()
                        .toUri()

        when:
            ResponseEntity<ReservationResponseDto> result = httpClient.postForEntity(
                    uri,
                    reservationRequest,
                    ReservationResponseDto.class)

        then:
            result.statusCode == HttpStatus.CREATED
            with (result.body) {
                reservationId != null
                tickets == new TicketsDto([new TicketDto(1, 3, TicketPricing.ADULT.name())])
                totalPrice == BigDecimal.valueOf(25)
                with (screeningDetails) {
                    movieTitle == "Lot"
                    room == Room.ROOM_A.name()
                    screeningTime == LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.DAYS)
                }
            }

        and:
            def updatedScreening = screeningFacade.getScreening(screening.getId())
            hasOneSeatReserved(updatedScreening)
    }

    def "should return UNPROCESSABLE_ENTITY with proper message for invalid data in request"() {
        given:
            ReservationRequestDto reservationRequest = prepareReservationRequestInvalid()

        and:
            URI uri = getBaseUri()
                    .path("reservations")
                    .build()
                    .toUri()

        when:
            ResponseEntity<ErrorResponse> result = httpClient.postForEntity(
                    uri,
                    reservationRequest,
                    ErrorResponse.class)

        then:
            result.statusCode == HttpStatus.UNPROCESSABLE_ENTITY
            result.body.message == "Every field should be filled"
    }

    def "should get reservation"() {
        given:
            Screening screening = createScreening()
            String reservationId = createReservation(screening.getId())
                    .getReservationId()

        and:
            URI uri = getBaseUri()
                    .pathSegment("reservations", reservationId)
                    .build()
                    .toUri()

        when:
            ResponseEntity<ReservationResponseDto> result = httpClient.getForEntity(
                    uri,
                    ReservationResponseDto.class)

        then:
            result.statusCode == HttpStatus.OK
            with (result.body) {
                reservationId != null
                tickets == new TicketsDto([new TicketDto(1, 3, TicketPricing.ADULT.name())])
                totalPrice == BigDecimal.valueOf(25)
                with (screeningDetails) {
                    movieTitle == "Lot"
                    room == Room.ROOM_A.name()
                    screeningTime == LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.DAYS)
                }
            }

    }

    def "should return NOT_FOUND with proper message for not found reservation"() {
        given:
            URI uri = getBaseUri()
                    .pathSegment("reservations", "id")
                    .build()
                    .toUri()

        when:
            ResponseEntity<ErrorResponse> result = httpClient.getForEntity(
                    uri,
                    ErrorResponse.class)

        then:
            result.statusCode == HttpStatus.NOT_FOUND
            result.body.message == "Could not find reservation with id: id"
    }

    def "should cancel reservation"() {
        given:
            Screening screening = createScreening()
            String reservationId = createReservation(screening.getId())
                    .getReservationId()

        and:
            URI uri = getBaseUri()
                    .pathSegment("reservations", reservationId)
                    .build()
                    .toUri()

        when:
            httpClient.delete(uri)

        then:
            reservationRepository.findAll().size() == 0

        and:
            ScreeningResponseDto updatedScreening = screeningFacade.getScreening(screening.getId())
            hasNoSeatsReserved(updatedScreening.getSeats())
    }

    ReservationResponseDto createReservation(String screeningId) {
        ReservationRequestDto reservationRequest = prepareReservationRequest(screeningId)
        reservationFacade.createReservation(reservationRequest)
    }

    ReservationRequestDto prepareReservationRequest(String screeningId) {
        new ReservationRequestDto.Builder()
                .withScreeningId(screeningId)
                .withUserName("UserName")
                .withUserSurname("User-Surname")
                .withTickets(new TicketsDto([new TicketDto(1, 3, TicketPricing.ADULT.name())]))
                .build()
    }

    ReservationRequestDto prepareReservationRequestInvalid() {
        new ReservationRequestDto.Builder()
                .withScreeningId("screeningId")
                .withUserSurname("Surname")
                .withUserName("name")
                .build()
    }

    boolean hasNoSeatsReserved(List<SeatDto> seats) {
        seats.stream()
                .filter({ it -> it.isReserved() })
                .collect(Collectors.toList())
                .isEmpty()
    }

    private boolean hasOneSeatReserved(ScreeningResponseDto updatedScreening) {
        updatedScreening.getSeats().contains(new SeatDto(1, 3, true))
    }

}
