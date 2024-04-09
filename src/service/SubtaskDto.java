package service;

public class SubtaskDto {
    private final Long id;
    private final String name;
    private final Long sequence;

    public SubtaskDto(Long id, String name, Long sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public Long getSequence() {
        return sequence;
    }

    public Long getId() {
        return id;
    }
}
