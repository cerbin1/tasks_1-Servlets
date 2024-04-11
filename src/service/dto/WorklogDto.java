package service.dto;

import java.time.LocalDate;

public class WorklogDto {
    private final Long id;
    private final LocalDate date;
    private final Long minutes;
    private final String comment;

    public WorklogDto(Long id, LocalDate date, Long minutes, String comment) {
        this.id = id;
        this.date = date;
        this.minutes = minutes;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getMinutes() {
        return minutes;
    }

    public String getComment() {
        return comment;
    }
}
