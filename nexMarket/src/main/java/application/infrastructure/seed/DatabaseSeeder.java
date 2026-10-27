package application.infrastructure.seed;

import application.domain.models.*;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.ports.out.PasswordServicePort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Semillas de base de datos para que el 100% de los endpoints funcionen al arrancar:
 * - Administrador (admin@nexmarket.com)
 * - Supervisor (supervisor@nexmarket.com)
 * - Operador Logístico (logistics@nexmarket.com)
 * - Vendedor (seller@nexmarket.com)
 * - Comprador (buyer@nexmarket.com)
 * - Comprador Bloqueado (blocked@nexmarket.com) para pruebas de autorización
 * - Producto de ejemplo (PROD-001) para pruebas del carrito
 *
 * Contraseña única académica para todos: Password123*
 * Idempotente por email e identificador.
 */
@Component
@Order(1)
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);
    private static final String DEFAULT_PASSWORD = "Password123*";

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final BuyerRepositoryPort buyerRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public DatabaseSeeder(UserRepositoryPort userRepositoryPort,
                          PasswordServicePort passwordServicePort,
                          BuyerRepositoryPort buyerRepositoryPort,
                          SellerRepositoryPort sellerRepositoryPort,
                          ProductRepositoryPort productRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordServicePort = passwordServicePort;
        this.buyerRepositoryPort = buyerRepositoryPort;
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public void run(String... args) {
        log.info("Iniciando DatabaseSeeder de nexMarket...");
        try {
            seedAdministrator();
            seedSupervisor();
            seedLogisticsOperator();
            Seller seller = seedSeller();
            seedBuyer();
            seedBlockedBuyer();
            seedSampleProduct(seller);
            log.info("DatabaseSeeder de nexMarket completado con éxito. Credenciales listas con contraseña: {}", DEFAULT_PASSWORD);
        } catch (Exception e) {
            log.warn("Aviso durante la ejecución de DatabaseSeeder: {}", e.getMessage());
        }
    }

    private void seedAdministrator() {
        String email = "admin@nexmarket.com";
        Administrator admin = new Administrator();
        admin.setIdentifier("USR-ADMIN-01");
        admin.setEmail(email);
        admin.setFullName("Administrador General");
        admin.setRole(UserRole.ADMINISTRATOR);
        admin.setStatus(UserStatus.ACTIVE);
        admin.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(admin);
    }

    private void seedSupervisor() {
        String email = "supervisor@nexmarket.com";
        Supervisor supervisor = new Supervisor();
        supervisor.setIdentifier("USR-SUPER-01");
        supervisor.setEmail(email);
        supervisor.setFullName("Supervisor Operativo");
        supervisor.setRole(UserRole.SUPERVISOR);
        supervisor.setStatus(UserStatus.ACTIVE);
        supervisor.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(supervisor);
    }

    private void seedLogisticsOperator() {
        String email = "logistics@nexmarket.com";
        LogisticsOperator operator = new LogisticsOperator();
        operator.setIdentifier("USR-LOGIS-01");
        operator.setEmail(email);
        operator.setFullName("Operador Logístico Central");
        operator.setRole(UserRole.LOGISTICS_OPERATOR);
        operator.setStatus(UserStatus.ACTIVE);
        operator.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(operator);
    }

    private Seller seedSeller() {
        String email = "seller@nexmarket.com";
        Seller seller = new Seller();
        seller.setIdentifier("USR-SELL-01");
        seller.setEmail(email);
        seller.setFullName("Vendedor Tecnológico S.A.S.");
        seller.setRole(UserRole.SELLER);
        seller.setStatus(UserStatus.ACTIVE);
        seller.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(seller);

        if (sellerRepositoryPort.findByIdentifier(seller).isEmpty()) {
            sellerRepositoryPort.save(seller);
            log.info("Vendedor registrado en repositorio: {}", seller.getIdentifier());
        }
        return seller;
    }

    private void seedBuyer() {
        String email = "buyer@nexmarket.com";
        Buyer buyer = new Buyer();
        buyer.setIdentifier("USR-BUY-01");
        buyer.setEmail(email);
        buyer.setFullName("Santiago Comprador");
        buyer.setRole(UserRole.BUYER);
        buyer.setStatus(UserStatus.ACTIVE);
        buyer.setPrimaryAddress("Calle 10 #20-30 Medellín");
        buyer.setCommercialStatus(BuyerCommercialStatus.ACTIVE);
        buyer.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(buyer);

        if (buyerRepositoryPort.findByIdentifier(buyer).isEmpty()) {
            buyerRepositoryPort.save(buyer);
            log.info("Comprador registrado en repositorio: {}", buyer.getIdentifier());
        }
    }

    private void seedBlockedBuyer() {
        String email = "blocked@nexmarket.com";
        Buyer buyer = new Buyer();
        buyer.setIdentifier("USR-BUY-BLOCKED");
        buyer.setEmail(email);
        buyer.setFullName("Comprador Bloqueado");
        buyer.setRole(UserRole.BUYER);
        buyer.setStatus(UserStatus.BLOCKED);
        buyer.setPrimaryAddress("Carrera 45 #30-12 Medellín");
        buyer.setCommercialStatus(BuyerCommercialStatus.RESTRICTED);
        buyer.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(buyer);

        if (buyerRepositoryPort.findByIdentifier(buyer).isEmpty()) {
            buyerRepositoryPort.save(buyer);
        }
    }

    private void seedSampleProduct(Seller seller) {
        PhysicalProduct query = new PhysicalProduct();
        query.setIdentifier("PROD-001");
        if (productRepositoryPort.findByIdentifier(query).isEmpty()) {
            PhysicalProduct product = new PhysicalProduct();
            product.setIdentifier("PROD-001");
            product.setName("Laptop Gamer Pro 16GB");
            product.setDescription("Laptop de alto rendimiento para desarrollo y gaming");
            product.setProductType(ProductType.PHYSICAL);
            product.setStatus(ProductStatus.PUBLISHED);
            product.setSeller(seller);
            product.setWeight(BigDecimal.valueOf(2.5));
            productRepositoryPort.save(product);
            log.info("Producto de ejemplo creado: PROD-001 (Laptop Gamer Pro 16GB)");
        }
    }

    private void saveIfAbsent(User user) {
        if (!userRepositoryPort.existsByEmail(user)) {
            userRepositoryPort.save(user);
            log.info("Usuario creado: {} ({}) con rol {}", user.getFullName(), user.getEmail(), user.getRole());
        }
    }
}
