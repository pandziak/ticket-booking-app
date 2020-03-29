package pl.pandziak.ticketbookingapp.domain.screening;

import java.util.Objects;

public final class Seat {

    private final int row;
    private final int column;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Seat seat = (Seat) o;
        return row == seat.row &&
                column == seat.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

}
