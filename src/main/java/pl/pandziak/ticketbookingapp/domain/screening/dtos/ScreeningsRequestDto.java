package pl.pandziak.ticketbookingapp.domain.screening.dtos;

import java.time.LocalDateTime;

public class ScreeningsRequestDto {

    private final LocalDateTime from;

    public ScreeningsRequestDto(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getFrom() {
        return from;
    }

}
