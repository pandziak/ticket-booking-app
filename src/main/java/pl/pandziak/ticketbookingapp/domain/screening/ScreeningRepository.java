package pl.pandziak.ticketbookingapp.domain.screening;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {

    Screening save(Screening screening);

    Optional<Screening> findById(String id);

    List<Screening> findAllForDay(LocalDate date);

    List<Screening> findAll();

    void deleteAll();

}
