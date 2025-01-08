package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme t JOIN FETCH r.time ti")
    List<Reservation> findAllWithDetails();

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme t JOIN FETCH r.time ti WHERE r.date = :date AND r.theme.id = :themeId")
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}

