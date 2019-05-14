package handcraftedcode.orderbook;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static handcraftedcode.orderbook.Order.Type.BUY;
import static handcraftedcode.orderbook.Order.Type.SELL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiveOrderBoardTest {

    private LiveOrderBoard orderBoard;

    @Before
    public void setupOrderBoard() {
        orderBoard = new LiveOrderBoard();
    }

    @Test
    public void orderBoardIsEmptyInitially() {
        Collection<Order> allOrders = orderBoard.getAllOrders();
        assertEquals(0, allOrders.size());
    }

    @Test
    public void canRegisterOrder() {
        Order order = order("user1", 3.5, 303, BUY);

        orderBoard.registerOrder(order);

        Collection<Order> allOrders = orderBoard.getAllOrders();
        assertTrue(allOrders.contains(order));
        assertEquals(1, allOrders.size());
    }

    @Test
    public void canCancelOrder() {

        Order order = order("userId", 3.5, 303, BUY);

        orderBoard.registerOrder(order);
        orderBoard.cancelOrder(order);

        Collection<Order> allOrders = orderBoard.getAllOrders();
        assertEquals(0, allOrders.size());
    }

    @Test
    public void canGetOrderSummary() {

        orderBoard.registerOrder(order("user1", 3.5, 306, SELL));
        orderBoard.registerOrder(order("user2", 1.2, 310, SELL));
        orderBoard.registerOrder(order("user3", 1.5, 307, SELL));
        orderBoard.registerOrder(order("user4", 2.0, 306, SELL));

        Map<Order.Type, Map<Double, Double>> summary = orderBoard.getSummary();

        assertEquals(3, summary.get(SELL).size());
    }

    @Test
    public void cancelledOrdersNotInOrderSummary() {

        orderBoard.registerOrder(order("user1", 2.0, 306, SELL));

        Order order = order("user2", 3.5, 306, SELL);
        orderBoard.registerOrder(order);
        orderBoard.cancelOrder(order);

        Map<Order.Type, Map<Double, Double>> summary = orderBoard.getSummary();

        assertEquals(1, summary.get(SELL).size());
        assertEquals(2.0, summary.get(SELL).get(306.0), 0.00001);
    }

    @Test
    public void orderSummaryDoesNotContainZeroQuantityItems() {

        Order order = order("user2", 3.5, 306, SELL);
        orderBoard.registerOrder(order);
        orderBoard.cancelOrder(order);

        Map<Order.Type, Map<Double, Double>> summary = orderBoard.getSummary();

        assertTrue(summary.get(SELL).isEmpty());
    }

    @Test
    public void sellOrderSummarySortedCorrectly() {

        orderBoard.registerOrder(order("user1", 3.5, 306, SELL));
        orderBoard.registerOrder(order("user2", 1.2, 310, SELL));
        orderBoard.registerOrder(order("user3", 1.5, 307, SELL));


        Map<Double, Double> sellSummary = orderBoard.getSummary().get(SELL);

        double lastPrice = 0;
        for (Double price: sellSummary.keySet()) {
            assertTrue(price > lastPrice);
            lastPrice = price;
        }
    }

    @Test
    public void buyOrderSummarySortedCorrectly() {

        orderBoard.registerOrder(order("user1", 3.5, 306, BUY));
        orderBoard.registerOrder(order("user2", 1.2, 310, BUY));
        orderBoard.registerOrder(order("user3", 1.5, 307, BUY));


        Map<Double, Double> buySummary = orderBoard.getSummary().get(BUY);

        double lastPrice = 999;
        for (Double price: buySummary.keySet()) {
            assertTrue(price < lastPrice);
            lastPrice = price;
        }
    }

    private Order order(String userId, double quantity, int price, Order.Type type) {
        return new Order(userId, quantity, price, type);
    }
}
