package roomescape.reservation;

import jakarta.persistence.*;
import roomescape.theme.Theme;
import roomescape.time.Time;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date;

    @ManyToOne(fetch = FetchType.LAZY) // Time 엔티티와 다대일 관계
    @JoinColumn(name = "time_id", nullable = false) // 외래 키 매핑
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY) // Theme 엔티티와 다대일 관계
    @JoinColumn(name = "theme_id", nullable = false) // 외래 키 매핑
    private Theme theme;

    public Reservation(Long id, String name, String date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String date, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
