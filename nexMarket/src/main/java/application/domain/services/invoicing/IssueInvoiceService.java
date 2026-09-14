package application.domain.services.invoicing;

import application.domain.models.Invoice;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.ports.out.InvoiceRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class IssueInvoiceService {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;

    public IssueInvoiceService(InvoiceRepositoryPort invoiceRepositoryPort,
                               OrderRepositoryPort orderRepositoryPort) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public Invoice issueInvoice(Order order) {
        Order existingOrder = orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado para facturación: " + order.getIdentifier()));

        if (existingOrder.getOrderStatus() == OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderStatusException("No se puede emitir factura para un pedido pendiente de pago.");
        }

        BigDecimal total = BigDecimal.ZERO;
        if (existingOrder.getItems() != null) {
            for (OrderItem item : existingOrder.getItems()) {
                if (item.getUnitPrice() != null && item.getQuantity() != null) {
                    BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    total = total.add(lineTotal);
                }
            }
        }

        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("El importe total de la factura debe ser mayor a cero.");
        }

        Invoice invoice = new Invoice();
        invoice.setIdentifier("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setOrder(existingOrder);
        invoice.setBuyer(existingOrder.getBuyer());
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setTotalAmount(total);

        return invoiceRepositoryPort.save(invoice);
    }
}
