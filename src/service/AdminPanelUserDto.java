package service;

public class AdminPanelUserDto {
    private final Long id;
    private final String email;
    private final String username;
    private final String name;
    private final String surname;
    private final Boolean active;
    private final Long messageCount;

    public AdminPanelUserDto(Long id, String email, String username, String name, String surname, Boolean active, Long messageCount) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.active = active;
        this.messageCount = messageCount;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Boolean getActive() {
        return active;
    }

    public Long getMessageCount() {
        return messageCount;
    }
}
