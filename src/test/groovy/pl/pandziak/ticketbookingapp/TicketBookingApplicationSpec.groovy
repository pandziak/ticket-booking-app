package pl.pandziak.ticketbookingapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.web.util.UriComponentsBuilder
import pl.pandziak.ticketbookingapp.domain.Room
import pl.pandziak.ticketbookingapp.domain.reservation.ReservationRepository
import pl.pandziak.ticketbookingapp.domain.screening.Screening
import pl.pandziak.ticketbookingapp.domain.screening.ScreeningRepository
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketBookingApplicationSpec extends Specification {

    @Autowired
    protected TestRestTemplate httpClient

    @Autowired
    protected ScreeningRepository screeningRepository

    @Autowired
    protected ReservationRepository reservationRepository

    @LocalServerPort
    protected int port

    protected UriComponentsBuilder getBaseUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
    }

    protected void createScreenings() {
        screeningRepository.save(
                new Screening.Builder()
                        .withId(UUID.randomUUID().toString())
                        .withMovieTitle("Lot")
                        .withScreeningTime(LocalDateTime.of(2020, 1, 1, 10, 30))
                        .withRoom(Room.ROOM_A)
                        .build())
        screeningRepository.save(
                new Screening.Builder()
                        .withId(UUID.randomUUID().toString())
                        .withMovieTitle("Powrót Jedi")
                        .withScreeningTime(LocalDateTime.of(2020, 1, 1, 16, 00))
                        .withRoom(Room.ROOM_B)
                        .build())
    }

    protected Screening createScreening() {
        screeningRepository.save(
                new Screening.Builder()
                        .withId("id")
                        .withMovieTitle("Lot")
                        .withScreeningTime(LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.DAYS))
                        .withRoom(Room.ROOM_A)
                        .withSeats([])
                        .build())
    }

}
