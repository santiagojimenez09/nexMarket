package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.BuyerJpaEntity;
import application.adapters.persistence.jpa.mappers.BuyerJpaMapper;
import application.adapters.persistence.jpa.mappers.UserJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaBuyerRepository;
import application.adapters.persistence.jpa.repositories.SpringDataJpaUserRepository;
import application.domain.models.Buyer;
import application.domain.ports.out.BuyerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BuyerJpaAdapter implements BuyerRepositoryPort {

    private final SpringDataJpaBuyerRepository buyerRepository;
    private final SpringDataJpaUserRepository userRepository;

    @Override
    public Buyer save(Buyer buyer) {
        if (buyer == null) return null;
        if (buyer.getIdentifier() != null) {
            userRepository.save(UserJpaMapper.toEntity(buyer));
        }
        BuyerJpaEntity entity = BuyerJpaMapper.toEntity(buyer);
        BuyerJpaEntity saved = buyerRepository.save(entity);
        return BuyerJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Buyer> findByIdentifier(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return Optional.empty();
        return buyerRepository.findById(buyer.getIdentifier()).map(BuyerJpaMapper::toDomain);
    }

    @Override
    public void update(Buyer buyer) {
        if (buyer != null && buyer.getIdentifier() != null) {
            buyerRepository.save(BuyerJpaMapper.toEntity(buyer));
            userRepository.save(UserJpaMapper.toEntity(buyer));
        }
    }
}
