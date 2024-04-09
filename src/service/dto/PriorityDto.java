package service.dto;

public class PriorityDto {
    private final Long id;
    private final String value;

    public PriorityDto(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
