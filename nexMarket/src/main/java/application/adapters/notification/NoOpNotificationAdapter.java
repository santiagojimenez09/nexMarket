package application.adapters.notification;

import application.domain.models.Notification;
import application.domain.ports.out.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class NoOpNotificationAdapter implements NotificationPort {

    @Override
    public void send(Notification notification) {
        // Adaptador de salida para entrega de notificaciones (email, SMS, push)
    }
}
