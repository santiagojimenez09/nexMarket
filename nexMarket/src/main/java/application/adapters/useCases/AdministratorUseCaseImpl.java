package application.adapters.useCases;

import application.domain.models.Administrator;
import application.domain.models.Buyer;
import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.models.Warehouse;
import application.domain.ports.in.AdministratorPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.services.buyer.ChangeBuyerCommercialStatusService;
import application.domain.services.returns.ApproveReturnService;
import application.domain.services.returns.ProcessRefundService;
import application.domain.services.returns.RejectRefundService;
import application.domain.services.returns.RejectReturnService;
import application.domain.services.seller.ChangeSellerStatusService;
import application.domain.services.seller.RegisterSellerService;
import application.domain.services.user.ChangeUserStatusService;
import application.domain.services.user.RegisterUserService;
import application.domain.services.warehouse.ChangeWarehouseStatusService;
import application.domain.services.warehouse.RegisterWarehouseService;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserStatus;
import application.domain.valueObjects.WarehouseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministratorUseCaseImpl implements AdministratorPort {

    private final RegisterUserService registerUserService;
    private final ChangeUserStatusService changeUserStatusService;
    private final ChangeBuyerCommercialStatusService changeBuyerCommercialStatusService;
    private final RegisterSellerService registerSellerService;
    private final ChangeSellerStatusService changeSellerStatusService;
    private final RegisterWarehouseService registerWarehouseService;
    private final ChangeWarehouseStatusService changeWarehouseStatusService;
    private final ApproveReturnService approveReturnService;
    private final RejectReturnService rejectReturnService;
    private final ProcessRefundService processRefundService;
    private final RejectRefundService rejectRefundService;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    private Administrator toAdmin(User adminUser) {
        Administrator admin = new Administrator();
        admin.setIdentifier(adminUser.getIdentifier());
        admin.setFullName(adminUser.getFullName());
        admin.setEmail(adminUser.getEmail());
        admin.setRole(adminUser.getRole());
        admin.setStatus(adminUser.getStatus());
        return admin;
    }

    @Override
    public User registerPlatformUser(User admin, User newUser, String rawPassword) {
        return registerUserService.registerUser(newUser, rawPassword);
    }

    @Override
    public void changeUserStatus(User admin, User user, UserStatus newStatus) {
        changeUserStatusService.changeStatus(user, newStatus);
    }

    @Override
    public void changeBuyerCommercialStatus(User admin, Buyer buyer, BuyerCommercialStatus newStatus) {
        changeBuyerCommercialStatusService.changeStatus(buyer, newStatus);
    }

    @Override
    public Seller registerSeller(User admin, Seller seller, String initialWarehouseName, String initialWarehouseAddress) {
        return registerSellerService.registerSeller(seller, toAdmin(admin), initialWarehouseName, initialWarehouseAddress);
    }

    @Override
    public void changeSellerStatus(User admin, Seller seller, UserStatus newStatus) {
        changeSellerStatusService.changeStatus(seller, newStatus);
    }

    @Override
    public Warehouse registerWarehouse(User admin, Warehouse warehouse) {
        return registerWarehouseService.registerWarehouse(warehouse);
    }

    @Override
    public void changeWarehouseStatus(User admin, Warehouse warehouse, WarehouseStatus newStatus) {
        changeWarehouseStatusService.changeStatus(warehouse, newStatus);
    }

    @Override
    public Return approveReturn(User admin, Return returnRequest) {
        return approveReturnService.approveReturn(returnRequest);
    }

    @Override
    public Return rejectReturn(User admin, Return returnRequest, String reason) {
        return rejectReturnService.rejectReturn(returnRequest, reason);
    }

    @Override
    public Refund processRefund(User admin, Return returnRequest, BigDecimal amount) {
        return processRefundService.processRefund(returnRequest, amount, toAdmin(admin));
    }

    @Override
    public Refund rejectRefund(User admin, Refund refund, String reason) {
        return rejectRefundService.rejectRefund(refund, toAdmin(admin), reason);
    }

    @Override
    public List<Seller> listSellers(User admin) {
        return sellerRepositoryPort.findByRegisteredBy(toAdmin(admin));
    }

    @Override
    public List<Warehouse> listWarehouses(User admin) {
        return warehouseRepositoryPort.findByType(new Warehouse());
    }
}
