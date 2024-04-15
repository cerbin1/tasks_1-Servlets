package service.dto;

public class UserDto {

    private final Long id;
    private final String name;
    private final String surname;
    private final String username;

    public UserDto(Long id, String name, String surname, String username) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }
}
