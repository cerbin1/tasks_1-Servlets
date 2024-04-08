package service;

public class SubtaskDto {
    private final String name;
    private final Long sequence;

    public SubtaskDto(String name, Long sequence) {
        this.name = name;
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public Long getSequence() {
        return sequence;
    }
}
