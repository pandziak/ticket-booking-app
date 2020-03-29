package pl.pandziak.ticketbookingapp.domain.screening;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
final class ScreeningInMemoryRepository implements ScreeningRepository {

    private final Map<String, Screening> screenings = new HashMap<>();

    @Override
    public Screening save(Screening screening) {
        screenings.put(screening.getId(), screening);
        return screening;
    }

    @Override
    public Optional<Screening> findById(String id) {
        return Optional.ofNullable(screenings.get(id));
    }

    @Override
    public List<Screening> findAllForDay(LocalDate date) {
        return findAll()
                .stream()
                .filter(s -> s.getScreeningTime().getDayOfWeek().equals(date.getDayOfWeek()))
                .collect(toList());
    }

    @Override
    public List<Screening> findAll() {
        return List.copyOf(screenings.values());
    }

    @Override
    public void deleteAll() {
        screenings.clear();
    }

}
