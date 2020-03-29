package pl.pandziak.ticketbookingapp.domain.reservation

import pl.pandziak.ticketbookingapp.domain.TicketPricing
import spock.lang.Specification

class TicketsSpec extends Specification {

    def "should calculate total price"() {
        given:
            Tickets tickets = tickets()

        when:
            BigDecimal totalPrice = tickets.calculateTotalPrice()

        then:
            totalPrice == BigDecimal.valueOf(55.5)
    }

    def "should return zero for no tickets"() {
        when:
            BigDecimal totalPrice = new Tickets([]).calculateTotalPrice()

        then:
            totalPrice == BigDecimal.ZERO
    }

    Tickets tickets() {
        new Tickets([
                new Ticket(1, 3, TicketPricing.ADULT),
                new Ticket(1, 4, TicketPricing.CHILD),
                new Ticket(1, 4, TicketPricing.STUDENT)
        ])
    }

}
