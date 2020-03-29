package pl.pandziak.ticketbookingapp.domain.reservation;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
final class ReservationInMemoryRepository implements ReservationRepository {

    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public void delete(Reservation reservation) {
        reservations.remove(reservation.getId());
    }

    @Override
    public void deleteAll() {
        reservations.clear();
    }

}
