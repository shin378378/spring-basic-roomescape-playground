package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.Authentication;
import roomescape.member.Member;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> myList(
            @Authentication Member member) {
        List<ReservationResponse> reservationResponsesByMemberName = reservationService.findAllByMemberName(member.getName());
        List<ReservationResponse> reservationResponsesByMemberId = reservationService.findAllByMemberId(member.getId());
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        reservationResponses.addAll(reservationResponsesByMemberName);
        reservationResponses.addAll(reservationResponsesByMemberId);
        return MyReservationResponse.from(reservationResponses);
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@Authentication Member member,
                                 @RequestBody ReservationRequest reservationRequest) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservationRequest.getName() == null) {
            reservationRequest = new ReservationRequest(
                    member.getName(),
                    reservationRequest.getDate(),
                    reservationRequest.getTheme(),
                    reservationRequest.getTime()
            );
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
