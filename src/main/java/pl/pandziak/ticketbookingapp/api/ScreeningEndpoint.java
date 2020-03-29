package pl.pandziak.ticketbookingapp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pandziak.ticketbookingapp.domain.screening.ScreeningFacade;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsRequestDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsResponseDto;
import pl.pandziak.ticketbookingapp.domain.screening.exceptions.InvalidDateFormatException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/screenings")
class ScreeningEndpoint {

    private final ScreeningFacade screeningFacade;

    ScreeningEndpoint(ScreeningFacade screeningFacade) {
        this.screeningFacade = screeningFacade;
    }

    @GetMapping("/{id}")
    ScreeningResponseDto getScreening(@PathVariable String id) {
        return screeningFacade.getScreening(id);
    }

    @GetMapping
    ScreeningsResponseDto getScreenings(@RequestParam("from") String from) {
        LocalDateTime parsedFrom = parseDate(from);
        return screeningFacade.getScreeningsForDayAfterGivenHour(new ScreeningsRequestDto(parsedFrom));
    }

    private LocalDateTime parseDate(String date) {
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(date);
        }
    }

}
