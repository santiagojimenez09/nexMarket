package application.domain.ports.out;

import application.domain.models.Notification;

public interface NotificationPort {
    void send(Notification notification);
}
