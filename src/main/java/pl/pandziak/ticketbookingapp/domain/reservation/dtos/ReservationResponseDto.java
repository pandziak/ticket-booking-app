package pl.pandziak.ticketbookingapp.domain.reservation.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningDetailsDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReservationResponseDto {

    private final String reservationId;
    private final ScreeningDetailsDto screeningDetails;
    private final TicketsDto tickets;
    private final BigDecimal totalPrice;
    private final String expirationAt;

    @JsonCreator
    public ReservationResponseDto(
            @JsonProperty("reservationId") String reservationId,
            @JsonProperty("screeningDetails") ScreeningDetailsDto screeningDetails,
            @JsonProperty("tickets") TicketsDto tickets,
            @JsonProperty("totalPrice") BigDecimal totalPrice,
            @JsonProperty("expirationAt") String expirationAt) {
        this.reservationId = reservationId;
        this.screeningDetails = screeningDetails;
        this.tickets = tickets;
        this.totalPrice = totalPrice;
        this.expirationAt = expirationAt;
    }

    private ReservationResponseDto(Builder builder) {
        this(
                builder.reservationId,
                builder.screeningDetails,
                builder.tickets,
                builder.totalPrice,
                builder.expirationAt != null ? builder.expirationAt.toString() : null);
    }

    public String getReservationId() {
        return reservationId;
    }

    public ScreeningDetailsDto getScreeningDetails() {
        return screeningDetails;
    }

    public TicketsDto getTickets() {
        return tickets;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getExpirationAt() {
        return expirationAt;
    }

    public static class Builder {

        private String reservationId;
        private ScreeningDetailsDto screeningDetails;
        private TicketsDto tickets;
        private BigDecimal totalPrice;
        private LocalDateTime expirationAt;

        public Builder withReservationId(String reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public Builder withScreeningDetails(ScreeningDetailsDto screeningDetails) {
            this.screeningDetails = screeningDetails;
            return this;
        }

        public Builder withTickets(TicketsDto tickets) {
            this.tickets = tickets;
            return this;
        }

        public Builder withTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder withExpirationAt(LocalDateTime expirationAt) {
            this.expirationAt = expirationAt;
            return this;
        }

        public ReservationResponseDto build() {
            return new ReservationResponseDto(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ReservationResponseDto that = (ReservationResponseDto) o;
        return Objects.equals(reservationId, that.reservationId) &&
                Objects.equals(screeningDetails, that.screeningDetails) &&
                Objects.equals(tickets, that.tickets) &&
                Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, screeningDetails, tickets, totalPrice);
    }

}
