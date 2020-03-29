package pl.pandziak.ticketbookingapp.domain.screening.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ScreeningResponseDto {

    private final ScreeningDetailsDto screeningDetails;
    private final List<SeatDto> seats;

    @JsonCreator
    public ScreeningResponseDto(
            @JsonProperty("screeningDetails") ScreeningDetailsDto screeningDetails,
            @JsonProperty("seats") List<SeatDto> seats) {
        this.screeningDetails = screeningDetails;
        this.seats = seats;
    }

    public ScreeningDetailsDto getScreeningDetails() {
        return screeningDetails;
    }

    public List<SeatDto> getSeats() {
        return seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ScreeningResponseDto that = (ScreeningResponseDto) o;
        return Objects.equals(screeningDetails, that.screeningDetails) &&
                Objects.equals(seats, that.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screeningDetails, seats);
    }

}
