package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeRepository.getById(reservationRequest.getTheme());
        Time time = timeRepository.getById(reservationRequest.getTime());
        Member member = memberRepository.findByName(reservationRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Reservation reservation = new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                member);

        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(),
                reservationRequest.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationResponse> findAllByMemberName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
