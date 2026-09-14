package application.domain.ports.in;

import application.domain.models.Administrator;
import application.domain.models.Seller;

public interface RegisterSellerUseCase {
    Seller registerSeller(Seller seller, Administrator administrator, String initialWarehouseName, String initialWarehouseAddress);
}
