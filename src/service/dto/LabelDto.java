package service.dto;

public class LabelDto {

    private final Long id;
    private final String name;

    public LabelDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
