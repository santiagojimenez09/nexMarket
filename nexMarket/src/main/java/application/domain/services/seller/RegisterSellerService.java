package application.domain.services.seller;

import application.domain.models.Administrator;
import application.domain.models.Seller;
import application.domain.models.Warehouse;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.in.RegisterSellerUseCase;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import application.domain.valueObjects.WarehouseStatus;
import application.domain.valueObjects.WarehouseType;

public class RegisterSellerService implements RegisterSellerUseCase {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public RegisterSellerService(SellerRepositoryPort sellerRepositoryPort,
                                 UserRepositoryPort userRepositoryPort,
                                 WarehouseRepositoryPort warehouseRepositoryPort) {
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public Seller registerSeller(Seller seller, Administrator administrator, String initialWarehouseName, String initialWarehouseAddress) {
        if (administrator == null || administrator.getRole() != UserRole.ADMINISTRATOR) {
            throw new UnauthorizedOperationException("Solo un Administrador puede registrar vendedores (OBJ-02, DOMINIO 3).");
        }
        if (seller == null) {
            throw new DomainException("El vendedor no puede ser nulo.");
        }
        if (userRepositoryPort.existsByIdentifier(seller)) {
            throw new DomainException("Ya existe un usuario con el identificador: " + seller.getIdentifier());
        }
        if (userRepositoryPort.existsByEmail(seller)) {
            throw new DomainException("Ya existe un usuario con el email: " + seller.getEmail());
        }

        seller.setRole(UserRole.SELLER);
        seller.setStatus(UserStatus.ACTIVE);
        seller.setRegisteredBy(administrator);

        Seller savedSeller = sellerRepositoryPort.save(seller);

        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier("WH-" + savedSeller.getIdentifier());
        warehouse.setName(initialWarehouseName != null ? initialWarehouseName : "Almacén Principal " + savedSeller.getFullName());
        warehouse.setAddress(initialWarehouseAddress != null ? initialWarehouseAddress : "Dirección no especificada");
        warehouse.setType(WarehouseType.SELLER);
        warehouse.setOwner(savedSeller);
        warehouse.setStatus(WarehouseStatus.ACTIVE);

        Warehouse savedWarehouse = warehouseRepositoryPort.save(warehouse);
        savedSeller.getWarehouses().add(savedWarehouse);

        return savedSeller;
    }
}
