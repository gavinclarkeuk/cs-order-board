package handcraftedcode.orderbook;

public class Order {
    private final String userId;
    private final double quantity;
    private final double price;
    private final Type type;

    public enum Type { SELL, BUY }

    public Order(String userId, double quantity, double price, Type type) {

        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }
}
