package pl.pandziak.ticketbookingapp.domain.reservation;

import pl.pandziak.ticketbookingapp.domain.Room;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.ReservationRequestDto;
import pl.pandziak.ticketbookingapp.domain.reservation.dtos.TicketsDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningResponseDto;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.SeatDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.Assert.isTrue;

final class ReservationValidator {

    private static final int MIN_USER_NAME_LEN = 3;
    private static final String PERMITTED_CHARACTERS = "^[a-zA-Z]*[ ]*[-]?[ ]*[a-zA-Z]*$";

    void validateInputFieldsNonNullOrEmpty(ReservationRequestDto reservationRequest) {
        isTrue(reservationRequest.isValid(), "Every field should be filled");
    }

    void validateUserName(String name) {
        isTrue(name.length() >= MIN_USER_NAME_LEN, "Name does not have the proper length of " + MIN_USER_NAME_LEN);
        isTrue(isFirstLetterUpperCase(name), "Name does not start with lower case");
        isTrue(name.matches(PERMITTED_CHARACTERS), "Illegal characters used in name");
    }

    void validateUserSurname(String surname) {
        isTrue(surname.length() >= MIN_USER_NAME_LEN, "Surname does not have the proper length of " + MIN_USER_NAME_LEN);
        isTrue(isFirstLetterUpperCase(surname), "Surname does not start with capital letter");
        isTrue(twoPartsOfSurnameStartsWithUpperCase(surname), "Both parts of surname need to start with capital letter");
        isTrue(surname.matches(PERMITTED_CHARACTERS), "Illegal characters used in surname");
    }

    void validateTickets(TicketsDto tickets) {
        isTrue(!tickets.empty(), "Reservation should include at least one ticket");
    }

    void validateTime(LocalDateTime screeningTime) {
        LocalDateTime allowedReservationTime = screeningTime.minusMinutes(15);
        LocalDateTime now = LocalDateTime.now();

        isTrue(now.isBefore(allowedReservationTime), "Reservations can be made only until 15 minutes before screening");
    }

    void validateChosenSeats(TicketsDto tickets, ScreeningResponseDto screeningResponse) {
        validateTickets(tickets);

        int maxColumn = Room.valueOf(screeningResponse.getScreeningDetails().getRoom()).getColumns();
        List<SeatDto> takenSeats = screeningResponse.getSeats();
        List<SeatDto> seatsToBook = tickets.getTickets()
                .stream()
                .map(t -> new SeatDto(t.getRow(), t.getColumn(), true))
                .collect(toList());

        isTrue(seatsNotTaken(takenSeats, seatsToBook), "This reservation includes already taken seats");
        isTrue(seatsTogether(seatsToBook), "Seats to book should be chosen next to each other");
        isTrue(seatsWithoutGapsBetweenAlreadyReserved(seatsToBook, takenSeats, maxColumn),"There cannot be a single gap between already reserved seats");
    }

    private boolean isFirstLetterUpperCase(String text) {
        return Character.isUpperCase(text.trim().charAt(0));
    }

    private boolean twoPartsOfSurnameStartsWithUpperCase(String surname) {
        return !surname.contains("-") || isFirstLetterUpperCase(surname.split("-")[1]);
    }

    private boolean seatsTogether(List<SeatDto> seatsToBook) {
        return theSameRow(seatsToBook) && seatsNextToEachOther(seatsToBook);
    }

    private boolean theSameRow(List<SeatDto> seatsToBook) {
        return seatsToBook.stream()
                .map(SeatDto::getRow)
                .count() == seatsToBook.size();
    }

    private boolean seatsNextToEachOther(List<SeatDto> seatsToBook) {
        List<Integer> columns = seatsToBook.stream()
                .map(SeatDto::getColumn)
                .collect(toList());

        Integer max = columns.stream().max(Integer::compareTo).get();
        Integer min = columns.stream().min(Integer::compareTo).get();

        List<Integer> range = IntStream.rangeClosed(min, max)
                .boxed()
                .collect(toList());

        return range.containsAll(columns);
    }

    private boolean seatsNotTaken(List<SeatDto> takenSeats, List<SeatDto> seatsToBook) {
        return seatsToBook.stream()
                .filter(takenSeats::contains)
                .collect(toSet())
                .isEmpty();
    }

    private boolean seatsWithoutGapsBetweenAlreadyReserved(List<SeatDto> seatsToBook, List<SeatDto> takenSeats, int maxColumnInRoom) {
        List<Integer> columns = seatsToBook.stream()
                .map(SeatDto::getColumn)
                .collect(toList());

        Integer max = columns.stream().max(Integer::compareTo).get();
        Integer min = columns.stream().min(Integer::compareTo).get();

        int row = seatsToBook.get(0).getRow();
        return validNeighbouringSeats(min, max, row, takenSeats, maxColumnInRoom)
                || oneSeatInTheRowLeft(row, seatsToBook, takenSeats, maxColumnInRoom);
    }

    private boolean oneSeatInTheRowLeft(int row, List<SeatDto> seatsToBook, List<SeatDto> seatsInCinema, int maxColumnInRoom) {
        return seatsInCinema.stream()
                .filter(seat -> seat.isReserved() && seat.getRow() == row)
                .count() + seatsToBook.size() == maxColumnInRoom - 1;
    }

    private boolean validNeighbouringSeats(int minColumn, int maxColumn, int row, List<SeatDto> allSeats, int maxColumnInRoom) {
        if (closestNeighbourIsEmpty(allSeats, row, minColumn, 1, minColumn - 1)) {
            if (nextNeighbourIsNotEmpty(allSeats, row, minColumn, 2, minColumn - 2)) {
                return false;
            }
        }

        if (closestNeighbourIsEmpty(allSeats, row, maxColumn, maxColumnInRoom, maxColumn + 1)) {
            if (nextNeighbourIsNotEmpty(allSeats, row, maxColumn, maxColumnInRoom - 1, maxColumn + 2)) {
                return false;
            }
        }

        return true;
    }

    private boolean nextNeighbourIsNotEmpty(List<SeatDto> allSeats, int row, int column, int i, int j) {
        return column != i && allSeats.contains(new SeatDto(row, j, true));
    }

    private boolean closestNeighbourIsEmpty(List<SeatDto> allSeats, int row, int column, int i, int j) {
        return column != i && !allSeats.contains(new SeatDto(row, j, true));
    }

}
