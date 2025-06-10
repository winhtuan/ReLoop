package Model.entity.pay;

import java.math.BigDecimal;

public class ShippingMethod {
    private int id;
    private String name;
    private String description;
    private BigDecimal cost;
    private int estimatedDays;
    private boolean isActive;

    public ShippingMethod() {}

    public ShippingMethod(int id, String name, String description, BigDecimal cost, int estimatedDays, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.estimatedDays = estimatedDays;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public int getEstimatedDays() { return estimatedDays; }
    public void setEstimatedDays(int estimatedDays) { this.estimatedDays = estimatedDays; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
