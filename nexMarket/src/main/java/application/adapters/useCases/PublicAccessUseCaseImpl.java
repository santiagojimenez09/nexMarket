package application.adapters.useCases;

import application.domain.models.Buyer;
import application.domain.models.Product;
import application.domain.models.User;
import application.domain.ports.in.PublicAccessPort;
import application.domain.services.buyer.RegisterBuyerService;
import application.domain.services.catalog.ConsultProductService;
import application.domain.services.catalog.ConsultPublicCatalogService;
import application.domain.services.user.AuthenticateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicAccessUseCaseImpl implements PublicAccessPort {

    private final AuthenticateUserService authenticateUserService;
    private final RegisterBuyerService registerBuyerService;
    private final ConsultPublicCatalogService consultPublicCatalogService;
    private final ConsultProductService consultProductService;

    @Override
    public String login(User user, String rawPassword) {
        return authenticateUserService.authenticate(user, rawPassword);
    }

    @Override
    public Buyer registerBuyer(Buyer buyer, String rawPassword) {
        return registerBuyerService.registerBuyer(buyer, rawPassword);
    }

    @Override
    public List<Product> consultPublicCatalog() {
        return consultPublicCatalogService.getPublicCatalog();
    }

    @Override
    public Product consultProductDetail(Product product) {
        return consultProductService.getByIdentifier(product);
    }
}
