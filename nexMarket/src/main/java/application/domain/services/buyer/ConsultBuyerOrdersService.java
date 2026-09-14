package application.domain.services.buyer;

import application.domain.models.Buyer;
import application.domain.models.Order;
import application.domain.models.User;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.UserRole;

import java.util.List;

public class ConsultBuyerOrdersService {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;

    public ConsultBuyerOrdersService(BuyerRepositoryPort buyerRepositoryPort,
                                     OrderRepositoryPort orderRepositoryPort) {
        this.buyerRepositoryPort = buyerRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public List<Order> consultOrders(Buyer buyer, User requestingUser) {
        Buyer existingBuyer = buyerRepositoryPort.findByIdentifier(buyer)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado: " + buyer.getIdentifier()));

        if (requestingUser.getRole() == UserRole.BUYER &&
                !requestingUser.getIdentifier().equals(existingBuyer.getIdentifier())) {
            throw new UnauthorizedOperationException("No tiene autorización para consultar los pedidos de otro comprador (DOMINIO 2).");
        }

        List<Order> orders = orderRepositoryPort.findByBuyer(existingBuyer);
        existingBuyer.setOrders(orders);
        return orders;
    }
}
