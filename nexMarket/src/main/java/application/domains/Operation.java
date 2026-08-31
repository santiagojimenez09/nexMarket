package application.domains;

import application.valueObjects.OperationType;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Operation {
    private String operationId;
    private OperationType operationType;
    private LocalDateTime executionDate;
    private User performedBy;
    private TrackableProcess affectedProcess;
}
