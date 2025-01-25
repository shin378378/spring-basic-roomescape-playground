package roomescape.waiting;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Waiting(final Long id, final String date, final Time time, final Theme theme, final Member member) {
        this.id = id;
        this.date = LocalDate.parse(date);
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Waiting(final String date, final Time time, final Theme theme, final Member member) {
        this.date = LocalDate.parse(date);
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Waiting() {
    }

    public Long getId() {
        return id;
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

    public Member getMember() {
        return member;
    }
}
