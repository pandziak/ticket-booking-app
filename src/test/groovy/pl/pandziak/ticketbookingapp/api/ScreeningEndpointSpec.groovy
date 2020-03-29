package pl.pandziak.ticketbookingapp.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.pandziak.ticketbookingapp.ErrorResponse
import pl.pandziak.ticketbookingapp.TicketBookingApplicationSpec
import pl.pandziak.ticketbookingapp.domain.Room
import pl.pandziak.ticketbookingapp.domain.screening.Screening
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsResponseDto

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ScreeningEndpointSpec extends TicketBookingApplicationSpec {

    @Autowired
    ApplicationContext applicationContext

    def setup() {
        screeningRepository.deleteAll()
    }

    def "should get all screenings after given hour"() {
        given:
            createScreenings()

        and:
            URI uri = getBaseUri()
                    .path("screenings")
                    .queryParam("from", "2020-01-01T15:30")
                    .build()
                    .toUri()

        when:
            ResponseEntity<ScreeningsResponseDto> result = httpClient.getForEntity(uri, ScreeningsResponseDto.class)

        then:
            result.statusCode == HttpStatus.OK
            result.body.getScreenings().size() == 1
            with (result.body) {
                screenings.get(0).movieTitle == "Powrót Jedi"
                screenings.get(0).screeningTime == LocalDateTime.of(2020, 1, 1, 16, 00)
                screenings.get(0).room == Room.ROOM_B.name()
            }
    }

    def "should return BAD_REQUEST with proper message for invalid date format"() {
        given:
            URI uri = getBaseUri()
                    .path("screenings")
                    .queryParam("from", "2020-01-01 15:30")
                    .build()
                    .toUri()

        when:
            ResponseEntity<ErrorResponse> result = httpClient.getForEntity(uri, ErrorResponse.class)

        then:
            result.statusCode == HttpStatus.BAD_REQUEST
            result.body.message == "Invalid date: 2020-01-01 15:30. Please use yyyy-MM-ddTHH:mm in 'from' parameter"
    }

    def "should get screening details"() {
        given:
            Screening screening = createScreening()

        and:
            URI uri = getBaseUri()
                        .pathSegment("screenings", screening.getId())
                        .build()
                        .toUri()

        when:
            ResponseEntity<ScreeningResponseDto> result = httpClient.getForEntity(uri, ScreeningResponseDto.class)

        then:
            result.statusCode == HttpStatus.OK
            with (result.body) {
                seats.size() == Room.ROOM_A.getColumns() * Room.ROOM_A.getRows()
                with (screeningDetails) {
                    movieTitle == "Lot"
                    screeningTime == LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.DAYS)
                    room == Room.ROOM_A.name()
                }
            }
    }

    def "should return NOT_FOUND with proper message for not found screening"() {
        given:
            URI uri = getBaseUri()
                    .pathSegment("screenings", "fakeId")
                    .build()
                    .toUri()

        when:
            ResponseEntity<ErrorResponse> result = httpClient.getForEntity(uri, ErrorResponse.class)

        then:
            result.statusCode == HttpStatus.NOT_FOUND
            result.body.message == "Could not find screening with id: fakeId"
    }

}
