package application.domains;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class TrackableProcess {
    private String identifier;
}
