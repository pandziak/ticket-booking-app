package pl.pandziak.ticketbookingapp.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class Reservation {

    private final String id;
    private final String screeningId;
    private final LocalDateTime createdAt;
    private final String userName;
    private final String userSurname;
    private final Tickets tickets;

    private Reservation(Builder builder) {
        this.id = builder.id;
        this.screeningId = builder.screeningId;
        this.createdAt = builder.createdAt;
        this.userName = builder.userName;
        this.userSurname = builder.userSurname;
        this.tickets = new Tickets(builder.tickets);
    }

    public String getId() {
        return id;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public Tickets getTickets() {
        return tickets;
    }

    public static class Builder {

        private String id;
        private String screeningId;
        private LocalDateTime createdAt;
        private String userName;
        private String userSurname;
        private List<Ticket> tickets;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withScreeningId(String screeningId) {
            this.screeningId = screeningId;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
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

        public Builder withTickets(List<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(screeningId, that.screeningId) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(userSurname, that.userSurname) &&
                Objects.equals(tickets, that.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, screeningId, createdAt, userName, userSurname, tickets);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", screeningId='" + screeningId + '\'' +
                ", createdAt=" + createdAt +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", tickets=" + tickets +
                '}';
    }

}
