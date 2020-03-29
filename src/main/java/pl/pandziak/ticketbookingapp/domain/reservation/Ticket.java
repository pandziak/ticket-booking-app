package pl.pandziak.ticketbookingapp.domain.reservation;

import pl.pandziak.ticketbookingapp.domain.TicketPricing;

import java.util.Objects;

public class Ticket {

    private final int row;
    private final int column;
    private final TicketPricing ticketPricing;

    public Ticket(int row, int column, TicketPricing ticketPricing) {
        this.row = row;
        this.column = column;
        this.ticketPricing = ticketPricing;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public TicketPricing getTicketPricing() {
        return ticketPricing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Ticket ticket = (Ticket) o;
        return row == ticket.row &&
                column == ticket.column &&
                ticketPricing == ticket.ticketPricing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, ticketPricing);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "row=" + row +
                ", column=" + column +
                ", ticketPricing=" + ticketPricing +
                '}';
    }

}
