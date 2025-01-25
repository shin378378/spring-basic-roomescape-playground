package roomescape.time;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value")
    private LocalTime value;

    public Time() {
    }

    public Time(Long id, String value) {
        this.id = id;
        this.value = LocalTime.parse(value);
    }

    public Time(String value) {
        this.value = LocalTime.parse(value);
    }

    public Long getId() {
        return id;
    }

    public String getValueByString() {
        return value.toString();
    }
}
