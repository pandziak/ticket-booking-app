package pl.pandziak.ticketbookingapp.domain.screening.dtos;

import java.time.LocalDateTime;

public class ScreeningRequestDto {

    private final String movieTitle;
    private final LocalDateTime screeningTime;
    private final String room;

    public ScreeningRequestDto(String movieTitle, LocalDateTime screeningTime, String room) {
        this.movieTitle = movieTitle;
        this.screeningTime = screeningTime;
        this.room = room;
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

}
