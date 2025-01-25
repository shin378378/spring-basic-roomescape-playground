package roomescape.reservation;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Reservation(final Long id, final String name, final String date, final Time time, final Theme theme, final Member member) {
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date);
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(final String name, final String date, final Time time, final Theme theme, final Member member) {
        this.name = name;
        this.date = LocalDate.parse(date);
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateByString() {
        return date.toString();
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
