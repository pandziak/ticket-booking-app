package pl.pandziak.ticketbookingapp.domain.reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(String id);

    List<Reservation> findAll();

    void delete(Reservation reservation);

    void deleteAll();

}
