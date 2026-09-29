package application.adapters.useCases;

import application.domain.exceptions.EntityNotFoundException;
import application.domain.models.Buyer;
import application.domain.models.Invoice;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.Product;
import application.domain.models.Return;
import application.domain.models.ShoppingCart;
import application.domain.models.User;
import application.domain.ports.in.BuyerPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.services.buyer.ConsultBuyerOrdersService;
import application.domain.services.buyer.ConsultBuyerService;
import application.domain.services.buyer.UpdateBuyerAddressesService;
import application.domain.services.cart.AddItemToCartService;
import application.domain.services.cart.ClearCartService;
import application.domain.services.cart.ConsultCartService;
import application.domain.services.cart.RemoveItemFromCartService;
import application.domain.services.cart.UpdateCartItemService;
import application.domain.services.invoicing.ConsultInvoiceService;
import application.domain.services.order.ConfirmOrderService;
import application.domain.services.order.ConsultOrderService;
import application.domain.services.returns.RequestReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerUseCaseImpl implements BuyerPort {

    private final ConsultBuyerService consultBuyerService;
    private final UpdateBuyerAddressesService updateBuyerAddressesService;
    private final ConsultCartService consultCartService;
    private final AddItemToCartService addItemToCartService;
    private final UpdateCartItemService updateCartItemService;
    private final RemoveItemFromCartService removeItemFromCartService;
    private final ClearCartService clearCartService;
    private final ConfirmOrderService confirmOrderService;
    private final ConsultOrderService consultOrderService;
    private final ConsultBuyerOrdersService consultBuyerOrdersService;
    private final ConsultInvoiceService consultInvoiceService;
    private final RequestReturnService requestReturnService;
    private final ReturnRepositoryPort returnRepositoryPort;

    @Override
    public Buyer consultMyProfile(User user) {
        Buyer target = new Buyer();
        target.setIdentifier(user.getIdentifier());
        return consultBuyerService.consultBuyer(target, user);
    }

    @Override
    public void updateMyAddresses(User user, String primaryAddress, List<String> additionalAddresses) {
        Buyer target = new Buyer();
        target.setIdentifier(user.getIdentifier());
        updateBuyerAddressesService.updateAddresses(target, primaryAddress, additionalAddresses);
    }

    @Override
    public ShoppingCart consultMyCart(User user) {
        Buyer target = consultMyProfile(user);
        return consultCartService.getCartByBuyer(target);
    }

    @Override
    public ShoppingCart addItemToCart(User user, Product product, int quantity) {
        Buyer target = consultMyProfile(user);
        return addItemToCartService.addItem(target, product, quantity);
    }

    @Override
    public ShoppingCart updateCartItem(User user, Product product, int quantity) {
        Buyer target = consultMyProfile(user);
        return updateCartItemService.updateItemQuantity(target, product, quantity);
    }

    @Override
    public ShoppingCart removeItemFromCart(User user, Product product) {
        Buyer target = consultMyProfile(user);
        return removeItemFromCartService.removeItem(target, product);
    }

    @Override
    public void clearCart(User user) {
        Buyer target = consultMyProfile(user);
        clearCartService.clearCart(target);
    }

    @Override
    public Order checkoutCart(User user) {
        Buyer target = consultMyProfile(user);
        return confirmOrderService.confirmOrder(target);
    }

    @Override
    public Order consultOrder(User user, Order order) {
        return consultOrderService.getByIdentifier(order);
    }

    @Override
    public List<Order> consultMyOrders(User user) {
        Buyer target = consultMyProfile(user);
        return consultBuyerOrdersService.consultOrders(target, user);
    }

    @Override
    public Invoice consultMyInvoice(User user, Order order) {
        return consultInvoiceService.getByOrder(order);
    }

    @Override
    public Return requestReturn(User user, Order order, List<OrderItem> items, String reason) {
        return requestReturnService.requestReturn(order, items, reason);
    }

    @Override
    public Return consultReturn(User user, Return returnRequest) {
        return returnRepositoryPort.findByIdentifier(returnRequest)
                .orElseThrow(() -> new EntityNotFoundException("Devolución no encontrada: " + returnRequest.getIdentifier()));
    }
}
