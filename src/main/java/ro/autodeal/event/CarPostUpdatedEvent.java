package ro.autodeal.event;

public class CarPostUpdatedEvent {

    private final String username;
    private final String userEmail;
    private final String message;

    public CarPostUpdatedEvent(String username, String userEmail, String message) {
        this.username = username;
        this.userEmail = userEmail;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getMessage() {
        return message;
    }
}