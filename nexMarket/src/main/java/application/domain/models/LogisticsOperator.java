package application.domain.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogisticsOperator extends User {
    private List<Warehouse> operatedWarehouses = new ArrayList<>();
}
