package service.dto;

public class TimeLoggedDto {
    private final String name;
    private final Long minutesLogged;

    public TimeLoggedDto(String name, Long minutesLogged) {
        this.name = name;
        this.minutesLogged = minutesLogged;
    }

    public String getName() {
        return name;
    }

    public Long getMinutesLogged() {
        return minutesLogged;
    }
}
