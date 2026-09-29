package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.LoginRequestDTO;
import application.adapters.rest.dtos.requests.RegisterBuyerRequestDTO;
import application.adapters.rest.dtos.responses.BuyerResponseDTO;
import application.adapters.rest.dtos.responses.LoginResponseDTO;
import application.adapters.rest.dtos.responses.ProductResponseDTO;
import application.adapters.rest.mappers.BuyerRestMapper;
import application.adapters.rest.mappers.ProductRestMapper;
import application.adapters.rest.mappers.UserRestMapper;
import application.domain.models.Buyer;
import application.domain.models.PhysicalProduct;
import application.domain.models.Product;
import application.domain.models.User;
import application.domain.ports.in.PublicAccessPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicAccessController {

    private final PublicAccessPort publicAccessPort;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        User user = UserRestMapper.toDomain(requestDTO);
        String token = publicAccessPort.login(user, requestDTO.getPassword());
        LoginResponseDTO response = UserRestMapper.toLoginResponseDTO(token, requestDTO.getIdentifier(), "AUTHENTICATED", 3600);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<BuyerResponseDTO> registerBuyer(@RequestBody RegisterBuyerRequestDTO requestDTO) {
        Buyer buyer = BuyerRestMapper.toDomain(requestDTO);
        Buyer saved = publicAccessPort.registerBuyer(buyer, requestDTO.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerRestMapper.toResponseDTO(saved));
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<ProductResponseDTO>> getPublicCatalog() {
        List<Product> catalog = publicAccessPort.consultPublicCatalog();
        List<ProductResponseDTO> response = catalog.stream()
                .map(ProductRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{identifier}")
    public ResponseEntity<ProductResponseDTO> getProductDetail(@PathVariable String identifier) {
        Product query = new PhysicalProduct();
        query.setIdentifier(identifier);
        Product product = publicAccessPort.consultProductDetail(query);
        return ResponseEntity.ok(ProductRestMapper.toResponseDTO(product));
    }
}
