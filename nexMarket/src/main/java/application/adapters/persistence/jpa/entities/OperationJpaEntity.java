package application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationJpaEntity {

    @Id
    @Column(name = "operation_id", length = 100, nullable = false, unique = true)
    private String operationId;

    @Column(name = "operation_type", length = 50, nullable = false)
    private String operationType;

    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @Column(name = "performed_by_identifier", length = 100)
    private String performedByIdentifier;

    @Column(name = "affected_process_identifier", length = 100)
    private String affectedProcessIdentifier;
}
