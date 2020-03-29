package pl.pandziak.ticketbookingapp.domain.screening;

import org.springframework.stereotype.Service;
import pl.pandziak.ticketbookingapp.domain.Room;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketsDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningDetailsDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningRequestDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsRequestDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningsResponseDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto;
import pl.pandziak.ticketbookingapp.domain.screening.exceptions.ScreeningNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
final class ScreeningFacadeImpl implements ScreeningFacade {

    private final ScreeningRepository screeningRepository;

    ScreeningFacadeImpl(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    @Override
    public ScreeningsResponseDto getScreeningsForDayAfterGivenHour(ScreeningsRequestDto screeningsRequest) {
        List<Screening> screenings = screeningRepository
                .findAllForDay(screeningsRequest.getFrom().toLocalDate())
                .stream()
                .filter(startsAtTheSameHourOrLaterThatDay(screeningsRequest.getFrom()))
                .sorted(new ScreeningByTitleAndTimeComparator())
                .collect(toList());

        return toScreeningsResponse(screenings);
    }

    @Override
    public ScreeningResponseDto getScreening(String id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new ScreeningNotFoundException(id));

        return toScreeningResponse(screening);
    }

    @Override
    public void createScreening(ScreeningRequestDto screeningRequest) {
        screeningRepository.save(new Screening.Builder()
                .withId(UUID.randomUUID().toString())
                .withMovieTitle(screeningRequest.getMovieTitle())
                .withScreeningTime(screeningRequest.getScreeningTime())
                .withRoom(Room.valueOf(screeningRequest.getRoom()))
                .build());
    }

    @Override
    public void bookScreeningSeats(String screeningId, TicketsDto tickets) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));

        screening.bookSeats(tickets.toSeats());
        screeningRepository.save(screening);
    }

    @Override
    public void unbookScreeningSeats(String screeningId, TicketsDto tickets) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));

        cancelBooking(tickets, screening);
        screeningRepository.save(screening);
    }

    private Predicate<? super Screening> startsAtTheSameHourOrLaterThatDay(LocalDateTime date) {
        LocalDateTime truncated = date.truncatedTo(ChronoUnit.HOURS);
        return s -> s.getScreeningTime().isEqual(truncated)
                || s.getScreeningTime().isAfter(truncated);
    }

    private ScreeningsResponseDto toScreeningsResponse(List<Screening> screenings) {
        return new ScreeningsResponseDto(
                screenings.stream()
                        .map(screening -> new ScreeningDetailsDto.Builder()
                                .withScreeningId(screening.getId())
                                .withMovieTitle(screening.getMovie())
                                .withScreeningTime(screening.getScreeningTime())
                                .withRoom(screening.getRoom())
                                .build())
                        .collect(toList())
        );
    }

    private ScreeningResponseDto toScreeningResponse(Screening screening) {
        return new ScreeningResponseDto(
                new ScreeningDetailsDto.Builder()
                        .withScreeningId(screening.getId())
                        .withMovieTitle(screening.getMovie())
                        .withScreeningTime(screening.getScreeningTime())
                        .withRoom(screening.getRoom())
                        .build(),
                prepareSeatsLayoutForRoom(screening)
        );
    }

    private List<SeatDto> prepareSeatsLayoutForRoom(Screening screening) {
        List<Seat> seatsTaken = screening.getSeatsTaken();
        Room room = screening.getRoom();

        List<SeatDto> result = new LinkedList<>();

        for (int i = 1; i <= room.getRows(); i++) {
            for (int j = 1; j <= room.getColumns(); j++) {
                boolean seatTaken = seatsTaken.contains(new Seat(i, j));
                result.add(new SeatDto(i, j, seatTaken));
            }
        }

        return result;
    }

    private void cancelBooking(TicketsDto tickets, Screening screening) {
        for (TicketDto ticket : tickets.getTickets()) {
            screening.unbookSeat(ticket.getRow(), ticket.getColumn());
        }
    }

}