package application.domain.ports.in;

import application.domain.models.Buyer;
import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.models.Warehouse;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserStatus;
import application.domain.valueObjects.WarehouseStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AdministratorPort {
    // User Administration
    User registerPlatformUser(User admin, User newUser, String rawPassword);
    void changeUserStatus(User admin, User user, UserStatus newStatus);
    void changeBuyerCommercialStatus(User admin, Buyer buyer, BuyerCommercialStatus newStatus);
    
    // Seller & Warehouse Incorporation
    Seller registerSeller(User admin, Seller seller, String initialWarehouseName, String initialWarehouseAddress);
    void changeSellerStatus(User admin, Seller seller, UserStatus newStatus);
    Warehouse registerWarehouse(User admin, Warehouse warehouse);
    void changeWarehouseStatus(User admin, Warehouse warehouse, WarehouseStatus newStatus);
    
    // Returns & Refunds
    Return approveReturn(User admin, Return returnRequest);
    Return rejectReturn(User admin, Return returnRequest, String reason);
    Refund processRefund(User admin, Return returnRequest, BigDecimal amount);
    Refund rejectRefund(User admin, Refund refund, String reason);
    
    // Consultations
    List<Seller> listSellers(User admin);
    List<Warehouse> listWarehouses(User admin);
}
