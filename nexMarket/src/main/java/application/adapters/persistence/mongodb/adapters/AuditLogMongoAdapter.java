package application.adapters.persistence.mongodb.adapters;

import application.adapters.persistence.mongodb.documents.AuditLogDocument;
import application.adapters.persistence.mongodb.mappers.AuditLogMongoMapper;
import application.adapters.persistence.mongodb.repositories.AuditLogMongoRepository;
import application.domain.models.AuditLog;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.ports.out.AuditLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuditLogMongoAdapter implements AuditLogRepositoryPort {

    private final AuditLogMongoRepository auditLogMongoRepository;

    @Override
    public AuditLog save(AuditLog auditLog) {
        if (auditLog == null) return null;
        AuditLogDocument doc = AuditLogMongoMapper.toDocument(auditLog);
        AuditLogDocument saved = auditLogMongoRepository.save(doc);
        return AuditLogMongoMapper.toDomain(saved);
    }

    @Override
    public List<AuditLog> findByUser(User user) {
        if (user == null || user.getIdentifier() == null) return Collections.emptyList();
        return auditLogMongoRepository.findByPerformedByIdentifier(user.getIdentifier()).stream()
                .map(AuditLogMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByProcess(TrackableProcess process) {
        if (process == null || process.getIdentifier() == null) return Collections.emptyList();
        return auditLogMongoRepository.findByAffectedProcessIdentifier(process.getIdentifier()).stream()
                .map(AuditLogMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByOperationType(AuditLog auditLog) {
        if (auditLog == null || auditLog.getOperationType() == null) return Collections.emptyList();
        return auditLogMongoRepository.findByOperationType(auditLog.getOperationType().name()).stream()
                .map(AuditLogMongoMapper::toDomain)
                .collect(Collectors.toList());
    }
}
