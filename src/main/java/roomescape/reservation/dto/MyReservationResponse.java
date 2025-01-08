package roomescape.reservation.dto;

import java.util.List;
import java.util.stream.Collectors;

public class MyReservationResponse {
    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long id, String theme, String date, String time, String status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public static List<MyReservationResponse> from(List<ReservationResponse> reservations) {
        return reservations.stream()
                .map(it -> new MyReservationResponse(it.getId(),
                        it.getTheme(),
                        it.getDate(),
                        it.getTime(),
                        "예약"))
                .collect(Collectors.toList());
    }
}
