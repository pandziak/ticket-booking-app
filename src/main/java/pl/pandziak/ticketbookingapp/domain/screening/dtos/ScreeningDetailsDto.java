package pl.pandziak.ticketbookingapp.domain.screening.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.pandziak.ticketbookingapp.domain.Room;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScreeningDetailsDto {

    private final String screeningId;
    private final String movieTitle;
    private final LocalDateTime screeningTime;
    private final String room;

    @JsonCreator
    public ScreeningDetailsDto(
            @JsonProperty("screeningId") String screeningId,
            @JsonProperty("movieTitle") String movieTitle,
            @JsonProperty("screeningTime") LocalDateTime screeningTime,
            @JsonProperty("room") String room) {
        this.screeningId = screeningId;
        this.movieTitle = movieTitle;
        this.screeningTime = screeningTime;
        this.room = room;
    }

    private ScreeningDetailsDto(Builder builder) {
        this(
                builder.screeningId,
                builder.movieTitle,
                builder.screeningTime,
                builder.room.name());
    }

    public String getScreeningId() {
        return screeningId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public String getRoom() {
        return room;
    }

    public static class Builder {

        private String screeningId;
        private String movieTitle;
        private LocalDateTime screeningTime;
        private Room room;

        public Builder withScreeningId(String screeningId) {
            this.screeningId = screeningId;
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

        public ScreeningDetailsDto build() {
            return new ScreeningDetailsDto(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ScreeningDetailsDto that = (ScreeningDetailsDto) o;
        return Objects.equals(movieTitle, that.movieTitle) &&
                Objects.equals(screeningTime, that.screeningTime) &&
                Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieTitle, screeningTime, room);
    }

}
