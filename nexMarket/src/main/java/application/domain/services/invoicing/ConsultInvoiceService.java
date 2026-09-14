package application.domain.services.invoicing;

import application.domain.models.Buyer;
import application.domain.models.Invoice;
import application.domain.models.Order;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.InvoiceRepositoryPort;

import java.util.List;

public class ConsultInvoiceService {

    private final InvoiceRepositoryPort invoiceRepositoryPort;

    public ConsultInvoiceService(InvoiceRepositoryPort invoiceRepositoryPort) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
    }

    public Invoice getByOrder(Order order) {
        return invoiceRepositoryPort.findByOrder(order)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada para el pedido especificado."));
    }

    public List<Invoice> getByBuyer(Buyer buyer) {
        return invoiceRepositoryPort.findByBuyer(buyer);
    }
}
