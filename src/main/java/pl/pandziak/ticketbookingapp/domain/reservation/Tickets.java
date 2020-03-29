package pl.pandziak.ticketbookingapp.domain.reservation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

class Tickets {

    private final List<Ticket> tickets;

    Tickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    BigDecimal calculateTotalPrice() {
        return tickets.stream()
                .map(ticket -> ticket.getTicketPricing().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tickets tickets1 = (Tickets) o;
        return Objects.equals(tickets, tickets1.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickets);
    }

    @Override
    public String toString() {
        return "Tickets{" +
                "tickets=" + tickets +
                '}';
    }

}
