package roomescape.reservation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByDateAndThemeIdAndTimeId(LocalDate date, Long themeId, Long timeId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    @EntityGraph(attributePaths = {"theme", "time", "member"})
    List<Reservation> findByMemberId(Long memberId);
}
