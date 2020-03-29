package pl.pandziak.ticketbookingapp.domain.reservation.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import pl.pandziak.ticketbookingapp.domain.TicketPricing;
import pl.pandziak.ticketbookingapp.domain.reservation.Reservation;
import pl.pandziak.ticketbookingapp.domain.reservation.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class ReservationRequestDto {

    private final String screeningId;
    private final String userName;
    private final String userSurname;
    private final TicketsDto tickets;

    @JsonCreator
    public ReservationRequestDto(@JsonProperty String screeningId,
                          @JsonProperty String userName,
                          @JsonProperty String userSurname,
                          @JsonProperty TicketsDto tickets) {
        this.screeningId = screeningId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.tickets = tickets;
    }

    private ReservationRequestDto(Builder builder) {
        this(
                builder.screeningId,
                builder.userName,
                builder.userSurname,
                builder.tickets);
    }

    public boolean isValid() {
        return StringUtils.isNoneBlank(userName, userSurname)
                && tickets != null;
    }

    public Reservation toReservation() {
        return new Reservation.Builder()
                .withId(UUID.randomUUID().toString())
                .withScreeningId(screeningId)
                .withUserName(userName)
                .withUserSurname(userSurname)
                .withCreatedAt(LocalDateTime.now())
                .withTickets(tickets.getTickets()
                        .stream()
                        .map(t -> new Ticket(t.getRow(), t.getColumn(), TicketPricing.valueOf(t.getTicketType())))
                        .collect(toList()))
                .build();
    }

    public String getScreeningId() {
        return screeningId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public TicketsDto getTickets() {
        return tickets;
    }

    public static class Builder {

        private String screeningId;
        private String userName;
        private String userSurname;
        private TicketsDto tickets;

        public Builder withScreeningId(String screeningId) {
            this.screeningId = screeningId;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withUserSurname(String userSurname) {
            this.userSurname = userSurname;
            return this;
        }

        public Builder withTickets(TicketsDto tickets) {
            this.tickets = tickets;
            return this;
        }

        public ReservationRequestDto build() {
            return new ReservationRequestDto(this);
        }

    }

}
