package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.SellerJpaEntity;
import application.adapters.persistence.jpa.mappers.SellerJpaMapper;
import application.adapters.persistence.jpa.mappers.UserJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaSellerRepository;
import application.adapters.persistence.jpa.repositories.SpringDataJpaUserRepository;
import application.domain.models.Administrator;
import application.domain.models.Seller;
import application.domain.ports.out.SellerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SellerJpaAdapter implements SellerRepositoryPort {

    private final SpringDataJpaSellerRepository sellerRepository;
    private final SpringDataJpaUserRepository userRepository;

    @Override
    public Seller save(Seller seller) {
        if (seller == null) return null;
        if (seller.getIdentifier() != null) {
            userRepository.save(UserJpaMapper.toEntity(seller));
        }
        SellerJpaEntity entity = SellerJpaMapper.toEntity(seller);
        SellerJpaEntity saved = sellerRepository.save(entity);
        return SellerJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Seller> findByIdentifier(Seller seller) {
        if (seller == null || seller.getIdentifier() == null) return Optional.empty();
        return sellerRepository.findById(seller.getIdentifier()).map(SellerJpaMapper::toDomain);
    }

    @Override
    public List<Seller> findByRegisteredBy(Administrator administrator) {
        if (administrator == null || administrator.getIdentifier() == null) {
            return sellerRepository.findAll().stream().map(SellerJpaMapper::toDomain).collect(Collectors.toList());
        }
        return sellerRepository.findByRegisteredByIdentifier(administrator.getIdentifier()).stream()
                .map(SellerJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Seller seller) {
        if (seller != null && seller.getIdentifier() != null) {
            sellerRepository.save(SellerJpaMapper.toEntity(seller));
            userRepository.save(UserJpaMapper.toEntity(seller));
        }
    }
}
