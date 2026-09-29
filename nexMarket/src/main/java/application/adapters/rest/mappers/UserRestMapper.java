package application.adapters.rest.mappers;

import application.adapters.rest.dtos.requests.LoginRequestDTO;
import application.adapters.rest.dtos.responses.LoginResponseDTO;
import application.adapters.rest.dtos.responses.UserResponseDTO;
import application.domain.models.Buyer;
import application.domain.models.User;

public class UserRestMapper {

    public static User toDomain(LoginRequestDTO dto) {
        if (dto == null) return null;
        Buyer user = new Buyer();
        user.setIdentifier(dto.getIdentifier());
        user.setEmail(dto.getIdentifier());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static LoginResponseDTO toLoginResponseDTO(String token, String userIdentifier, String role, long expiresInSeconds) {
        return LoginResponseDTO.builder()
                .token(token)
                .userIdentifier(userIdentifier)
                .role(role)
                .expiresInSeconds(expiresInSeconds)
                .build();
    }

    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .identifier(user.getIdentifier())
                .name(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .build();
    }
}
