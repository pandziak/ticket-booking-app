package pl.pandziak.ticketbookingapp.domain.screening;

import pl.pandziak.ticketbookingapp.domain.Room;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Screening {

    private final String id;
    private final String movie;
    private final LocalDateTime screeningTime;
    private final Room room;
    private final List<Seat> seatsTaken;

    private Screening(Builder builder) {
        this.id = builder.id;
        this.movie = builder.movieTitle;
        this.screeningTime = builder.screeningTime;
        this.room = builder.room;
        this.seatsTaken = builder.seats != null ? builder.seats : new ArrayList<>();
    }

    void bookSeats(List<SeatDto> seats) {
        seats.forEach(seat -> bookSeat(seat.getRow(), seat.getColumn()));
    }

    private void bookSeat(int row, int column) {
        seatsTaken.add(new Seat(row, column));
    }

    public void unbookSeat(int row, int column) {
        seatsTaken.remove(new Seat(row, column));
    }

    public String getId() {
        return id;
    }

    public String getMovie() {
        return movie;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public Room getRoom() {
        return room;
    }

    public List<Seat> getSeatsTaken() {
        return seatsTaken;
    }

    public static class Builder {

        private String id;
        private String movieTitle;
        private LocalDateTime screeningTime;
        private Room room;
        private List<Seat> seats;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withMovieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withScreeningTime(LocalDateTime screeningTime) {
            this.screeningTime = screeningTime;
            return this;
        }

        public Builder withRoom(Room room) {
            this.room = room;
            return this;
        }

        public Builder withSeats(List<Seat> seats) {
            this.seats = seats;
            return this;
        }

        public Screening build() {
            return new Screening(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Screening screening = (Screening) o;
        return Objects.equals(id, screening.id) &&
                Objects.equals(movie, screening.movie) &&
                Objects.equals(screeningTime, screening.screeningTime) &&
                room == screening.room &&
                Objects.equals(seatsTaken, screening.seatsTaken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, screeningTime, room, seatsTaken);
    }

    @Override
    public String toString() {
        return "Screening{" +
                "id='" + id + '\'' +
                ", movie='" + movie + '\'' +
                ", screeningTime=" + screeningTime +
                ", room=" + room +
                ", seatsTaken=" + seatsTaken +
                '}';
    }

}
