package application.infrastructure.seed;

import application.domain.models.*;
import application.domain.ports.out.PasswordServicePort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Semillas de base de datos para que el 100% de los endpoints funcionen al arrancar:
 * - Administrador (admin@nexmarket.com)
 * - Supervisor (supervisor@nexmarket.com)
 * - Operador Logístico (logistics@nexmarket.com)
 * - Vendedor (seller@nexmarket.com)
 * - Comprador (buyer@nexmarket.com)
 * - Comprador Bloqueado (blocked@nexmarket.com) para pruebas de autorización
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

    public DatabaseSeeder(UserRepositoryPort userRepositoryPort,
                          PasswordServicePort passwordServicePort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordServicePort = passwordServicePort;
    }

    @Override
    public void run(String... args) {
        log.info("Iniciando DatabaseSeeder de nexMarket...");
        try {
            seedAdministrator();
            seedSupervisor();
            seedLogisticsOperator();
            seedSeller();
            seedBuyer();
            seedBlockedBuyer();
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

    private void seedSeller() {
        String email = "seller@nexmarket.com";
        Seller seller = new Seller();
        seller.setIdentifier("USR-SELL-01");
        seller.setEmail(email);
        seller.setFullName("Vendedor Tecnológico S.A.S.");
        seller.setRole(UserRole.SELLER);
        seller.setStatus(UserStatus.ACTIVE);
        seller.setPassword(passwordServicePort.encrypt(DEFAULT_PASSWORD));
        saveIfAbsent(seller);
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
    }

    private void saveIfAbsent(User user) {
        if (!userRepositoryPort.existsByEmail(user)) {
            userRepositoryPort.save(user);
            log.info("Usuario creado: {} ({}) con rol {}", user.getFullName(), user.getEmail(), user.getRole());
        }
    }
}
