package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.UserJpaEntity;
import application.adapters.persistence.jpa.mappers.UserJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaUserRepository;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {

    private final SpringDataJpaUserRepository userRepository;

    @Override
    public User save(User user) {
        if (user == null) return null;
        UserJpaEntity entity = UserJpaMapper.toEntity(user);
        UserJpaEntity saved = userRepository.save(entity);
        return UserJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByIdentifier(User user) {
        if (user == null || user.getIdentifier() == null) return Optional.empty();
        return userRepository.findById(user.getIdentifier()).map(UserJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(User user) {
        if (user == null || user.getEmail() == null) return Optional.empty();
        return userRepository.findByEmail(user.getEmail()).map(UserJpaMapper::toDomain);
    }

    @Override
    public boolean existsByIdentifier(User user) {
        return user != null && user.getIdentifier() != null && userRepository.existsByIdentifier(user.getIdentifier());
    }

    @Override
    public boolean existsByEmail(User user) {
        return user != null && user.getEmail() != null && userRepository.existsByEmail(user.getEmail());
    }

    @Override
    public List<User> findByRole(User user) {
        if (user == null || user.getRole() == null) return Collections.emptyList();
        return userRepository.findByRole(user.getRole().name()).stream()
                .map(UserJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(User user) {
        if (user != null && user.getIdentifier() != null) {
            userRepository.save(UserJpaMapper.toEntity(user));
        }
    }
}
