package application.adapters.persistence.jpa.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartJpaEntity {

    @Id
    @Column(name = "buyer_identifier", length = 100, nullable = false, unique = true)
    private String buyerIdentifier;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shopping_cart_items", joinColumns = @JoinColumn(name = "cart_buyer_identifier"))
    @Builder.Default
    private List<CartItemEmbeddable> items = new ArrayList<>();
}
