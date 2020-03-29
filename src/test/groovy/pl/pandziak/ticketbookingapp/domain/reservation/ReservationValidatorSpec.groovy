package pl.pandziak.ticketbookingapp.domain.reservation

import pl.pandziak.ticketbookingapp.domain.Room
import pl.pandziak.ticketbookingapp.domain.TicketPricing
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketDto
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketsDto
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningDetailsDto
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

import static pl.pandziak.ticketbookingapp.domain.TicketPricing.ADULT

@Unroll
class ReservationValidatorSpec extends Specification {

    ReservationValidator validator = new ReservationValidator()

    def "should validate user name #name"() {
        when:
            validator.validateUserName(name)

        then:
            noExceptionThrown()

        where:
            name << ["Bob", "Alice"]
    }

    def "should throw exception on #name user name"() {
        when:
            validator.validateUserName(name)

        then:
            thrown(IllegalArgumentException)

        where:
            name << ["Al", "", "clara"]
    }

    def "should throw exception for too short name #name"() {
        when:
            validator.validateUserName(name)

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Name does not have the proper length of 3"

        where:
            name << ["G", "Al", ""]
    }

    def "should throw exception for name #name starting with lower case"() {
        when:
            validator.validateUserName("pola")

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Name does not start with lower case"
    }

    def "should throw exception for illegal characters used in name #name"() {
        when:
            validator.validateUserName(name)

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Illegal characters used in name"

        where:
            name << ["Alice6", "Ja\$"]
    }

    def "should validate user surname #surname"() {
        when:
            validator.validateUserSurname(surname)

        then:
            noExceptionThrown()

        where:
            surname << ["Bob", "Marley", "Bob-Marley", "Mickey - Mouse"]
    }

    def "should throw exception for too short surname #surname"() {
        when:
            validator.validateUserSurname(surname)

        then:
            def error = thrown(IllegalArgumentException)
            error.message ==  "Surname does not have the proper length of 3"

        where:
            surname << ["G", "Al", ""]
    }

    def "should throw exception for lower case in second part of surname #surname"() {
        when:
            validator.validateUserSurname(surname)

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Both parts of surname need to start with capital letter"

        where:
            surname << ["Donald-duck", "Mc - donald"]
    }

    def "should throw exception for illegal characters used in surname #surname"() {
        when:
            validator.validateUserSurname(surname)

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Illegal characters used in surname"

        where:
            surname << ["Alice6", "Ja\$"]
    }

    def "should validate reservation tickets"() {
        when:
            validator.validateTickets(new TicketsDto([new TicketDto(1, 1, ADULT.name())]))

        then:
            noExceptionThrown()
    }

    def "should throw exception on invalid reservation tickets"() {
        when:
            validator.validateTickets(new TicketsDto([]))

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Reservation should include at least one ticket"
    }

    def "should validate reservation time"() {
        when:
            validator.validateTime(LocalDateTime.now().plusMinutes(30))

        then:
            noExceptionThrown()
    }

    def "should throw exception for making too late reservation"() {
        when:
            validator.validateTime(LocalDateTime.now().plusMinutes(10))

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "Reservations can be made only until 15 minutes before screening"
    }

    def "should validate reservation seats"() {
        given:
            ScreeningResponseDto screeningResponse = getScreeningResponse()
            TicketsDto tickets = new TicketsDto([
                    new TicketDto(2, 4, ADULT.name()),
                    new TicketDto(2, 5, ADULT.name())
            ])

        when:
            validator.validateChosenSeats(tickets, screeningResponse)

        then:
            noExceptionThrown()
    }

    def "should validate reservation seat when two seats left in the middle"() {
        given:
            ScreeningResponseDto screeningResponse = getScreeningResponseWithOnlyTwoSeatsLeftInTheRow()
            TicketsDto tickets = new TicketsDto([new TicketDto(2, 4, ADULT.name())])

        when:
            validator.validateChosenSeats(tickets, screeningResponse)

        then:
            noExceptionThrown()
    }

    def "should throw exception for overlapping seats"() {
        given:
            ScreeningResponseDto screeningResponse = getScreeningResponse()
            TicketsDto tickets = new TicketsDto([new TicketDto(2, 3, ADULT.name())])

        when:
            validator.validateChosenSeats(tickets, screeningResponse)

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "This reservation includes already taken seats"
    }

    def "should throw exception for leaving a gap between seats"() {
        given:
            ScreeningResponseDto screeningResponse = getScreeningResponse()
            TicketsDto tickets = new TicketsDto([new TicketDto(2, 5, ADULT.name())])

        when:
            validator.validateChosenSeats(tickets, screeningResponse)

        then:
            def error = thrown(IllegalArgumentException)
            error.message == "There cannot be a single gap between already reserved seats"
    }

    ScreeningResponseDto getScreeningResponse() {
        new ScreeningResponseDto(
                new ScreeningDetailsDto.Builder()
                        .withRoom(Room.ROOM_A)
                        .build(),
                [
                        new SeatDto(2, 2, true),
                        new SeatDto(2, 3, true)
                ]
        )
    }

    ScreeningResponseDto getScreeningResponseWithOnlyTwoSeatsLeftInTheRow() {
        new ScreeningResponseDto(
                new ScreeningDetailsDto.Builder()
                        .withRoom(Room.ROOM_B)
                        .build(),
                [
                        new SeatDto(2, 1, true),
                        new SeatDto(2, 2, true),
                        new SeatDto(2, 5, true)
                ]
        )
    }

}
