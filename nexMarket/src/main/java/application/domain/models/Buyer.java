package application.domain.models;

import application.domain.valueObjects.BuyerCommercialStatus;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Buyer extends User {
    private String primaryAddress;
    private List<String> additionalAddresses = new ArrayList<>();
    private BuyerCommercialStatus commercialStatus;
    private ShoppingCart cart;
    private List<Order> orders = new ArrayList<>();
}
