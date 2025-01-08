package roomescape.time;

import jakarta.persistence.*;

@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String time_value;

    @Column(nullable = false)
    private boolean deleted = false;

    public Time() {
    }

    public Time(Long id, String value) {
        this.id = id;
        this.time_value = value;
    }

    public Time(String value) {
        this.time_value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return time_value;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
