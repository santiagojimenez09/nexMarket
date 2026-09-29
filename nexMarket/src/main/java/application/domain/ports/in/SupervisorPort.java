package application.domain.ports.in;

import application.domain.models.AuditLog;
import application.domain.models.Operation;
import application.domain.models.Order;
import application.domain.models.TrackableProcess;
import application.domain.models.User;

import java.util.List;
import java.util.Map;

public interface SupervisorPort {
    Map<String, Object> generateOperationalSummaryReport(User supervisor);
    List<AuditLog> consultAuditTrailByProcess(User supervisor, TrackableProcess process);
    List<AuditLog> consultAuditTrailByUser(User supervisor, User targetUser);
    List<Operation> consultOperations(User supervisor, Operation query);
    List<Order> consultAllOrders(User supervisor);
}
