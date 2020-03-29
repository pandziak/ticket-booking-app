package pl.pandziak.ticketbookingapp.domain.screening


import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto
import spock.lang.Specification

class ScreeningSpec extends Specification {

    def "should book seats"() {
        given:
            Screening screening = prepareScreening()

        when:
            screening.bookSeats(seats())

        then:
            screening.getSeatsTaken() == [
                    new Seat(1, 1),
                    new Seat(1, 2)
            ]
    }

    def "should unbook seats"() {
        given:
            Screening screening = prepareScreening()
            screening.bookSeats(seats())

        when:
            screening.unbookSeat(1, 1)
            screening.unbookSeat(1, 2)

        then:
            screening.getSeatsTaken() == []
    }

    List<SeatDto> seats() {
        [
                new SeatDto(1, 1, true),
                new SeatDto(1, 2, true)
        ]
    }

    Screening prepareScreening() {
        return new Screening.Builder().build()
    }

}
