package application.domain.ports.in;

import application.domain.models.Buyer;
import application.domain.models.Invoice;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.Product;
import application.domain.models.Return;
import application.domain.models.ShoppingCart;
import application.domain.models.User;
import java.util.List;

public interface BuyerPort {
    Buyer consultMyProfile(User user);
    void updateMyAddresses(User user, String primaryAddress, List<String> additionalAddresses);
    
    // Shopping Cart
    ShoppingCart consultMyCart(User user);
    ShoppingCart addItemToCart(User user, Product product, int quantity);
    ShoppingCart updateCartItem(User user, Product product, int quantity);
    ShoppingCart removeItemFromCart(User user, Product product);
    void clearCart(User user);
    
    // Orders
    Order checkoutCart(User user);
    Order consultOrder(User user, Order order);
    List<Order> consultMyOrders(User user);
    
    // Invoices
    Invoice consultMyInvoice(User user, Order order);
    
    // Returns
    Return requestReturn(User user, Order order, List<OrderItem> items, String reason);
    Return consultReturn(User user, Return returnRequest);
}
