package roomescape.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.jwt.JwtService;
import roomescape.jwt.JwtUtil;
import roomescape.member.MemberDao;
import roomescape.member.Member;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberDao memberDao;

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping
    public ResponseEntity create(@CookieValue(name = "token", required = true) String token, @RequestBody ReservationRequest reservationRequest) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (reservationRequest.getName() == null) {
                Long userId = jwtService.getUserIdFromToken(token);
                Member member = memberDao.findById(userId);
                if (member == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                reservationRequest.setName(member.getName());
        }

        ReservationResponse reservation = reservationService.save(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
