package application.domain.ports.in;

import application.domain.models.Buyer;
import application.domain.models.Product;
import application.domain.models.User;
import java.util.List;

public interface PublicAccessPort {
    String login(User user, String rawPassword);
    Buyer registerBuyer(Buyer buyer, String rawPassword);
    List<Product> consultPublicCatalog();
    Product consultProductDetail(Product product);
}
