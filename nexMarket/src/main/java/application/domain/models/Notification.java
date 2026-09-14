package application.domain.models;

import application.domain.valueObjects.NotificationChannel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Notification {
    private String identifier;
    private User recipient;
    private NotificationChannel channel;
    private String subject;
    private String message;
    private LocalDateTime sentAt;
}
