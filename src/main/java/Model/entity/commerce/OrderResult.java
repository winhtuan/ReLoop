package Model.entity.commerce;

public class OrderResult {

    public String orderId;
    public int amount;

    public OrderResult(String orderId, int amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
