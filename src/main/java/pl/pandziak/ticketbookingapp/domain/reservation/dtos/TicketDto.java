package pl.pandziak.ticketbookingapp.domain.reservation.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto;

import java.util.Objects;

public class TicketDto {

    private final int row;
    private final int column;
    private final String ticketType;

    @JsonCreator
    public TicketDto(@JsonProperty int row,
                     @JsonProperty int column,
                     @JsonProperty String ticketType) {
        this.row = row;
        this.column = column;
        this.ticketType = ticketType;
    }

    SeatDto toSeat() {
        return new SeatDto(row, column, true);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getTicketType() {
        return ticketType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TicketDto ticketDto = (TicketDto) o;
        return row == ticketDto.row &&
                column == ticketDto.column &&
                Objects.equals(ticketType, ticketDto.ticketType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, ticketType);
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "row=" + row +
                ", column=" + column +
                ", ticketType=" + ticketType +
                '}';
    }

}
