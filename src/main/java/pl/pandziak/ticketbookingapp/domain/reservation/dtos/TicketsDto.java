package pl.pandziak.ticketbookingapp.domain.reservation.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class TicketsDto {

    private final List<TicketDto> tickets;

    @JsonCreator
    public TicketsDto(@JsonProperty List<TicketDto> tickets) {
        this.tickets = tickets;
    }

    public List<SeatDto> toSeats() {
        return tickets.stream()
                .map(TicketDto::toSeat)
                .collect(toList());
    }

    public boolean empty() {
        return getTickets().isEmpty();
    }

    public List<TicketDto> getTickets() {
        return tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TicketsDto tickets1 = (TicketsDto) o;
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
