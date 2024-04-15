package service.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TasksCountForDateDto {
    private final LocalDate date;
    private final Long count;

    public TasksCountForDateDto(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TasksCountForDateDto that = (TasksCountForDateDto) o;
        return Objects.equals(date, that.date) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, count);
    }

}