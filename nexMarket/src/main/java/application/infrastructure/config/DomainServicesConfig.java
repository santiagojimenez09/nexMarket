package application.infrastructure.config;

import application.domain.ports.out.*;
import application.domain.services.authorization.ValidatePermissionsService;
import application.domain.services.buyer.*;
import application.domain.services.cart.*;
import application.domain.services.catalog.*;
import application.domain.services.inventory.*;
import application.domain.services.invoicing.*;
import application.domain.services.logistics.*;
import application.domain.services.operation.*;
import application.domain.services.order.*;
import application.domain.services.returns.*;
import application.domain.services.seller.*;
import application.domain.services.user.*;
import application.domain.services.warehouse.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServicesConfig {

    // --- User Subdomain ---
    @Bean
    public AuthenticateUserService authenticateUserService(UserRepositoryPort userRepositoryPort,
                                                           PasswordServicePort passwordServicePort,
                                                           JwtServicePort jwtServicePort) {
        return new AuthenticateUserService(userRepositoryPort, passwordServicePort, jwtServicePort);
    }

    @Bean
    public RegisterUserService registerUserService(UserRepositoryPort userRepositoryPort,
                                                   PasswordServicePort passwordServicePort) {
        return new RegisterUserService(userRepositoryPort, passwordServicePort);
    }

    @Bean
    public ChangeUserStatusService changeUserStatusService(UserRepositoryPort userRepositoryPort) {
        return new ChangeUserStatusService(userRepositoryPort);
    }

    @Bean
    public ConsultUserService consultUserService(UserRepositoryPort userRepositoryPort) {
        return new ConsultUserService(userRepositoryPort);
    }

    @Bean
    public UpdateUserService updateUserService(UserRepositoryPort userRepositoryPort) {
        return new UpdateUserService(userRepositoryPort);
    }

    // --- Buyer Subdomain ---
    @Bean
    public RegisterBuyerService registerBuyerService(BuyerRepositoryPort buyerRepositoryPort,
                                                     UserRepositoryPort userRepositoryPort,
                                                     PasswordServicePort passwordServicePort) {
        return new RegisterBuyerService(buyerRepositoryPort, userRepositoryPort, passwordServicePort);
    }

    @Bean
    public ConsultBuyerService consultBuyerService(BuyerRepositoryPort buyerRepositoryPort) {
        return new ConsultBuyerService(buyerRepositoryPort);
    }

    @Bean
    public UpdateBuyerAddressesService updateBuyerAddressesService(BuyerRepositoryPort buyerRepositoryPort) {
        return new UpdateBuyerAddressesService(buyerRepositoryPort);
    }

    @Bean
    public ConsultBuyerOrdersService consultBuyerOrdersService(BuyerRepositoryPort buyerRepositoryPort,
                                                               OrderRepositoryPort orderRepositoryPort) {
        return new ConsultBuyerOrdersService(buyerRepositoryPort, orderRepositoryPort);
    }

    @Bean
    public ChangeBuyerCommercialStatusService changeBuyerCommercialStatusService(BuyerRepositoryPort buyerRepositoryPort) {
        return new ChangeBuyerCommercialStatusService(buyerRepositoryPort);
    }

    // --- Seller Subdomain ---
    @Bean
    public RegisterSellerService registerSellerService(SellerRepositoryPort sellerRepositoryPort,
                                                       UserRepositoryPort userRepositoryPort,
                                                       WarehouseRepositoryPort warehouseRepositoryPort) {
        return new RegisterSellerService(sellerRepositoryPort, userRepositoryPort, warehouseRepositoryPort);
    }

    @Bean
    public ConsultSellerService consultSellerService(SellerRepositoryPort sellerRepositoryPort) {
        return new ConsultSellerService(sellerRepositoryPort);
    }

    @Bean
    public ConsultSellerProductsService consultSellerProductsService(SellerRepositoryPort sellerRepositoryPort,
                                                                     ProductRepositoryPort productRepositoryPort) {
        return new ConsultSellerProductsService(sellerRepositoryPort, productRepositoryPort);
    }

    @Bean
    public ChangeSellerStatusService changeSellerStatusService(SellerRepositoryPort sellerRepositoryPort) {
        return new ChangeSellerStatusService(sellerRepositoryPort);
    }

    // --- Warehouse Subdomain ---
    @Bean
    public RegisterWarehouseService registerWarehouseService(WarehouseRepositoryPort warehouseRepositoryPort) {
        return new RegisterWarehouseService(warehouseRepositoryPort);
    }

    @Bean
    public ConsultWarehouseService consultWarehouseService(WarehouseRepositoryPort warehouseRepositoryPort) {
        return new ConsultWarehouseService(warehouseRepositoryPort);
    }

    @Bean
    public ChangeWarehouseStatusService changeWarehouseStatusService(WarehouseRepositoryPort warehouseRepositoryPort) {
        return new ChangeWarehouseStatusService(warehouseRepositoryPort);
    }

    // --- Catalog Subdomain ---
    @Bean
    public RegisterProductService registerProductService(ProductRepositoryPort productRepositoryPort,
                                                         SellerRepositoryPort sellerRepositoryPort) {
        return new RegisterProductService(productRepositoryPort, sellerRepositoryPort);
    }

    @Bean
    public ConsultProductService consultProductService(ProductRepositoryPort productRepositoryPort) {
        return new ConsultProductService(productRepositoryPort);
    }

    @Bean
    public ConsultPublicCatalogService consultPublicCatalogService(ProductRepositoryPort productRepositoryPort) {
        return new ConsultPublicCatalogService(productRepositoryPort);
    }

    @Bean
    public PublishProductService publishProductService(ProductRepositoryPort productRepositoryPort) {
        return new PublishProductService(productRepositoryPort);
    }

    @Bean
    public SuspendProductService suspendProductService(ProductRepositoryPort productRepositoryPort) {
        return new SuspendProductService(productRepositoryPort);
    }

    @Bean
    public DiscontinueProductService discontinueProductService(ProductRepositoryPort productRepositoryPort) {
        return new DiscontinueProductService(productRepositoryPort);
    }

    // --- Inventory Subdomain ---
    @Bean
    public RegisterInventoryService registerInventoryService(InventoryRepositoryPort inventoryRepositoryPort) {
        return new RegisterInventoryService(inventoryRepositoryPort);
    }

    @Bean
    public ConsultInventoryService consultInventoryService(InventoryRepositoryPort inventoryRepositoryPort) {
        return new ConsultInventoryService(inventoryRepositoryPort);
    }

    @Bean
    public RegisterInboundMovementService registerInboundMovementService(InventoryRepositoryPort inventoryRepositoryPort,
                                                                         InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        return new RegisterInboundMovementService(inventoryRepositoryPort, inventoryMovementRepositoryPort);
    }

    @Bean
    public RegisterSaleOutboundMovementService registerSaleOutboundMovementService(InventoryRepositoryPort inventoryRepositoryPort,
                                                                                   InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        return new RegisterSaleOutboundMovementService(inventoryRepositoryPort, inventoryMovementRepositoryPort);
    }

    @Bean
    public RegisterReturnInboundMovementService registerReturnInboundMovementService(InventoryRepositoryPort inventoryRepositoryPort,
                                                                                     InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        return new RegisterReturnInboundMovementService(inventoryRepositoryPort, inventoryMovementRepositoryPort);
    }

    @Bean
    public RegisterInventoryAdjustmentService registerInventoryAdjustmentService(InventoryRepositoryPort inventoryRepositoryPort,
                                                                                 InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        return new RegisterInventoryAdjustmentService(inventoryRepositoryPort, inventoryMovementRepositoryPort);
    }

    @Bean
    public ReserveInventoryService reserveInventoryService(InventoryRepositoryPort inventoryRepositoryPort,
                                                           InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        return new ReserveInventoryService(inventoryRepositoryPort, inventoryMovementRepositoryPort);
    }

    // --- Shopping Cart Subdomain ---
    @Bean
    public AddItemToCartService addItemToCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort,
                                                     ProductRepositoryPort productRepositoryPort,
                                                     BuyerRepositoryPort buyerRepositoryPort) {
        return new AddItemToCartService(shoppingCartRepositoryPort, productRepositoryPort, buyerRepositoryPort);
    }

    @Bean
    public ConsultCartService consultCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        return new ConsultCartService(shoppingCartRepositoryPort);
    }

    @Bean
    public UpdateCartItemService updateCartItemService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        return new UpdateCartItemService(shoppingCartRepositoryPort);
    }

    @Bean
    public RemoveItemFromCartService removeItemFromCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        return new RemoveItemFromCartService(shoppingCartRepositoryPort);
    }

    @Bean
    public ClearCartService clearCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        return new ClearCartService(shoppingCartRepositoryPort);
    }

    // --- Order Subdomain ---
    @Bean
    public ConfirmOrderService confirmOrderService(OrderRepositoryPort orderRepositoryPort,
                                                   ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        return new ConfirmOrderService(orderRepositoryPort, shoppingCartRepositoryPort);
    }

    @Bean
    public ConfirmPaymentService confirmPaymentService(OrderRepositoryPort orderRepositoryPort) {
        return new ConfirmPaymentService(orderRepositoryPort);
    }

    @Bean
    public DispatchOrderService dispatchOrderService(OrderRepositoryPort orderRepositoryPort) {
        return new DispatchOrderService(orderRepositoryPort);
    }

    @Bean
    public ConfirmDeliveryService confirmDeliveryService(OrderRepositoryPort orderRepositoryPort) {
        return new ConfirmDeliveryService(orderRepositoryPort);
    }

    @Bean
    public ConsultOrderService consultOrderService(OrderRepositoryPort orderRepositoryPort) {
        return new ConsultOrderService(orderRepositoryPort);
    }

    // --- Invoicing Subdomain ---
    @Bean
    public IssueInvoiceService issueInvoiceService(InvoiceRepositoryPort invoiceRepositoryPort,
                                                   OrderRepositoryPort orderRepositoryPort) {
        return new IssueInvoiceService(invoiceRepositoryPort, orderRepositoryPort);
    }

    @Bean
    public ConsultInvoiceService consultInvoiceService(InvoiceRepositoryPort invoiceRepositoryPort) {
        return new ConsultInvoiceService(invoiceRepositoryPort);
    }

    // --- Logistics Subdomain ---
    @Bean
    public PrepareShipmentService prepareShipmentService(ShipmentRepositoryPort shipmentRepositoryPort,
                                                         OrderRepositoryPort orderRepositoryPort,
                                                         WarehouseRepositoryPort warehouseRepositoryPort) {
        return new PrepareShipmentService(shipmentRepositoryPort, orderRepositoryPort, warehouseRepositoryPort);
    }

    @Bean
    public DispatchShipmentService dispatchShipmentService(ShipmentRepositoryPort shipmentRepositoryPort) {
        return new DispatchShipmentService(shipmentRepositoryPort);
    }

    @Bean
    public ConfirmShipmentDeliveryService confirmShipmentDeliveryService(ShipmentRepositoryPort shipmentRepositoryPort,
                                                                         ConfirmDeliveryService confirmDeliveryService) {
        return new ConfirmShipmentDeliveryService(shipmentRepositoryPort, confirmDeliveryService);
    }

    @Bean
    public ConsultShipmentService consultShipmentService(ShipmentRepositoryPort shipmentRepositoryPort) {
        return new ConsultShipmentService(shipmentRepositoryPort);
    }

    // --- Returns and Refunds Subdomain ---
    @Bean
    public RequestReturnService requestReturnService(ReturnRepositoryPort returnRepositoryPort,
                                                     OrderRepositoryPort orderRepositoryPort,
                                                     BusinessConfigurationPort businessConfigurationPort) {
        return new RequestReturnService(returnRepositoryPort, orderRepositoryPort, businessConfigurationPort);
    }

    @Bean
    public ApproveReturnService approveReturnService(ReturnRepositoryPort returnRepositoryPort) {
        return new ApproveReturnService(returnRepositoryPort);
    }

    @Bean
    public RejectReturnService rejectReturnService(ReturnRepositoryPort returnRepositoryPort) {
        return new RejectReturnService(returnRepositoryPort);
    }

    @Bean
    public ProcessRefundService processRefundService(RefundRepositoryPort refundRepositoryPort,
                                                     ReturnRepositoryPort returnRepositoryPort) {
        return new ProcessRefundService(refundRepositoryPort, returnRepositoryPort);
    }

    @Bean
    public RejectRefundService rejectRefundService(RefundRepositoryPort refundRepositoryPort) {
        return new RejectRefundService(refundRepositoryPort);
    }

    // --- Operations & Audit Subdomain ---
    @Bean
    public RegisterOperationAndAuditService registerOperationAndAuditService(OperationRepositoryPort operationRepositoryPort,
                                                                             AuditLogRepositoryPort auditLogRepositoryPort) {
        return new RegisterOperationAndAuditService(operationRepositoryPort, auditLogRepositoryPort);
    }

    @Bean
    public ConsultAuditLogService consultAuditLogService(AuditLogRepositoryPort auditLogRepositoryPort) {
        return new ConsultAuditLogService(auditLogRepositoryPort);
    }

    @Bean
    public GenerateAdministrativeReportService generateAdministrativeReportService(OperationRepositoryPort operationRepositoryPort) {
        return new GenerateAdministrativeReportService(operationRepositoryPort);
    }

    // --- Authorization Subdomain ---
    @Bean
    public ValidatePermissionsService validatePermissionsService(AuthorizationPort authorizationPort) {
        return new ValidatePermissionsService(authorizationPort);
    }
}
