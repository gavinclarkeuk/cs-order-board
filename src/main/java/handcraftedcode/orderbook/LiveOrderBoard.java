package handcraftedcode.orderbook;

import java.util.*;

public class LiveOrderBoard {

    private final Set<Order> orders = new HashSet<>();
    private final Map<Order.Type, Map<Double, Double>> summary;

    public LiveOrderBoard() {
        summary = new EnumMap<>(Order.Type.class);
        summary.put(Order.Type.SELL, new TreeMap<>());
        summary.put(Order.Type.BUY, new TreeMap<>(Comparator.reverseOrder()));
    }

    public void registerOrder(Order order) {
        orders.add(order);
        updateSummary(order.getType(), order.getPrice(), order.getQuantity());
    }

    public Collection<Order> getAllOrders() {
        return orders;
    }

    public boolean cancelOrder(Order order) {
        if (orders.remove(order)) {
            updateSummary(order.getType(), order.getPrice(), -order.getQuantity());

            return true;
        }
        return false;
    }

    private void updateSummary(Order.Type type, double price, double quantity) {
        Map<Double, Double> typeSummary = summary.get(type);
        typeSummary.merge(price, quantity, Double::sum);
        typeSummary.remove(price, 0.0);
    }

    public Map<Order.Type, Map<Double, Double>> getSummary() {
        // Summary is a simple map of maps. Client code can reformat easily as required.
        // Key for the first map is order type, the inner map is from price to quantity.
        return summary;
    }
}
