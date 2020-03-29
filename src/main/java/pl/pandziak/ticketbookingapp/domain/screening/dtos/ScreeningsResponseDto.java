package pl.pandziak.ticketbookingapp.domain.screening.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ScreeningsResponseDto {

    private final List<ScreeningDetailsDto> screenings;

    @JsonCreator
    public ScreeningsResponseDto(@JsonProperty("screenings") List<ScreeningDetailsDto> screenings) {
        this.screenings = screenings;
    }

    public List<ScreeningDetailsDto> getScreenings() {
        return screenings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ScreeningsResponseDto that = (ScreeningsResponseDto) o;
        return Objects.equals(screenings, that.screenings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenings);
    }

}
