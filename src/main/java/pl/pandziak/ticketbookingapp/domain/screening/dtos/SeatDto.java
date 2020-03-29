package pl.pandziak.ticketbookingapp.domain.screening.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SeatDto {

    private final int row;
    private final int column;
    private final boolean reserved;

    @JsonCreator
    public SeatDto(@JsonProperty int row,
                   @JsonProperty int column,
                   @JsonProperty boolean reserved) {
        this.row = row;
        this.column = column;
        this.reserved = reserved;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isReserved() {
        return reserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SeatDto seatDto = (SeatDto) o;
        return row == seatDto.row &&
                column == seatDto.column &&
                reserved == seatDto.reserved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, reserved);
    }

    @Override
    public String toString() {
        return "SeatDto{" +
                "row=" + row +
                ", column=" + column +
                ", reserved=" + reserved +
                '}';
    }

}
