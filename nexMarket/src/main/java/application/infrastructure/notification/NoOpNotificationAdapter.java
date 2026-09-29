package application.infrastructure.notification;

import application.domain.models.Notification;
import application.domain.ports.out.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class NoOpNotificationAdapter implements NotificationPort {

    @Override
    public void send(Notification notification) {
        // No-op placeholder for notification delivery (email, SMS, push)
    }
}
