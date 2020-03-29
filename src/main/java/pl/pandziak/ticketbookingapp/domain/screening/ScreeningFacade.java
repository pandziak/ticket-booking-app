package pl.pandziak.ticketbookingapp.domain.screening;

import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketsDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningRequestDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsRequestDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsResponseDto;

public interface ScreeningFacade {

    ScreeningsResponseDto getScreeningsForDayAfterGivenHour(ScreeningsRequestDto screeningsRequest);

    ScreeningResponseDto getScreening(String id);

    void createScreening(ScreeningRequestDto screeningRequest);

    void bookScreeningSeats(String screeningId, TicketsDto tickets);

    void unbookScreeningSeats(String screeningId, TicketsDto tickets);

}
