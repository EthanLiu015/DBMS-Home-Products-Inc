import java.sql.*;
import java.util.*;

/**
 * Service class for Order
 * @author Ethan Liu
 * @author Matthew Carrieri
 * I have neither given nor received any unauthorized aid
 */
public class OrderService {

    /**
     * method to get all the orders
     * @param return a list of all orders
     */
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        
        // query to get all info about orders
        String query = 
            "SELECT o.*, c.FirstName, c.LastName," + 
                   "op.ProductID, op.QuantityOrdered, op.QuotedPrice, p.ProductDescription," +
                   "pay.PaymentID, pay.PaymentDate, pay.CardHolder, pay.Amount, " +
                   "pay.Method, pay.CardNumber, pay.ExpirationDate, pay.BooleanCreditCard " +
            "FROM tblOrder o " +
            "LEFT JOIN tblCustomer c ON o.CustomerID = c.CustomerID " +
            "LEFT JOIN tblOrdersProducts op ON o.OrderID = op.OrderID " +
            "LEFT JOIN tblProduct p ON op.ProductID = p.ProductID " +
            "LEFT JOIN tblPayment pay ON o.OrderID = pay.OrderID " +
            "ORDER BY o.OrderID";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            int currentOrderId = -1;
            Order currentOrder = null;
            
            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                if (orderId != currentOrderId) {
                    currentOrder = new Order();
                    currentOrder.setOrderID(orderId);
                    currentOrder.setCustomerID(rs.getInt("CustomerID"));
                    currentOrder.setOrderDate(rs.getDate("OrderDate"));
                    currentOrder.setShippingDate(rs.getDate("ShippingDate"));
                    currentOrder.setStatus(rs.getString("OrderStatus"));
                    currentOrder.setShippingMethod(rs.getString("ShippingMethod"));
                    currentOrder.setSalesTax(rs.getDouble("SalesTax"));
                    currentOrder.setCustomerFirstName(rs.getString("FirstName"));
                    currentOrder.setCustomerLastName(rs.getString("LastName"));
                    orders.add(currentOrder);
                    currentOrderId = orderId;
                }
                
                // order item within order class
                String productId = rs.getString("ProductID");
                if (productId != null) {
                    Order.OrderItem item = new Order.OrderItem();
                    item.setProductID(productId);
                    item.setProductDescription(rs.getString("ProductDescription"));
                    item.setQuantityOrdered(rs.getInt("QuantityOrdered"));
                    item.setQuotedPrice(rs.getDouble("QuotedPrice"));
                    currentOrder.addOrderItem(item);
                }
                
                // order payment within payment class
                int paymentId = rs.getInt("PaymentID");
                if (!rs.wasNull()) {
                    Order.Payment payment = new Order.Payment();
                    payment.setPaymentID(paymentId);
                    payment.setPaymentDate(rs.getDate("PaymentDate"));
                    payment.setCardHolder(rs.getString("CardHolder"));
                    payment.setAmount(rs.getDouble("Amount"));
                    payment.setPaymentMethod(rs.getString("Method"));
                    payment.setCardNumber(rs.getString("CardNumber"));
                    payment.setExpirationDate(rs.getDate("ExpirationDate"));
                    payment.setIsCreditCard(rs.getBoolean("BooleanCreditCard"));
                    currentOrder.addPayment(payment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


    /**
     * method to add an order to the database
     * @param order is the order to add
     * @return a boolean if the order was successfully added
     */
    public static boolean addOrder(Order order) {
        String orderQuery = 
            "INSERT INTO tblOrder (CustomerID, OrderDate, ShippingDate, OrderStatus, " +
            "ShippingMethod, SalesTax) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
            DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword())) {

            connection.setAutoCommit(false);

            try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery, 
                Statement.RETURN_GENERATED_KEYS)) {

                orderStmt.setInt(1, order.getCustomerID());
                orderStmt.setDate(2, order.getOrderDate());
                orderStmt.setDate(3, order.getShippingDate());
                orderStmt.setString(4, order.getStatus());
                orderStmt.setString(5, order.getShippingMethod());
                orderStmt.setDouble(6, order.getSalesTax());
                
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    String itemQuery = 
                        "INSERT INTO tblOrdersProducts (OrderID, ProductID, QuantityOrdered, " +
                        "QuotedPrice) VALUES (?, ?, ?, ?)";

                    try (PreparedStatement itemStmt = connection.prepareStatement(itemQuery)) {
                        for (Order.OrderItem item : order.getOrderItems()) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setString(2, item.getProductID());
                            itemStmt.setInt(3, item.getQuantityOrdered());
                            itemStmt.setDouble(4, item.getQuotedPrice());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    String paymentQuery =
                        "INSERT INTO tblPayment (OrderID, PaymentDate, CardHolder, Amount, " +
                        "Method, CardNumber, ExpirationDate, BooleanCreditCard) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement paymentStmt = connection.prepareStatement(paymentQuery)) {
                        for (Order.Payment payment : order.getPayments()) {
                            paymentStmt.setInt(1, orderId);
                            paymentStmt.setDate(2, payment.getPaymentDate());
                            paymentStmt.setString(3, payment.getCardHolder());
                            paymentStmt.setDouble(4, payment.getAmount());
                            paymentStmt.setString(5, payment.getPaymentMethod());
                            paymentStmt.setString(6, payment.getCardNumber());
                            paymentStmt.setDate(7, payment.getExpirationDate());
                            paymentStmt.setBoolean(8, payment.getIsCreditCard());
                            paymentStmt.addBatch();
                        }
                        paymentStmt.executeBatch();
                    }

                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to check if a customer is valid
     * @param customerID customer to check
     * @return a boolean if the customer is valid
     */
    public static boolean isValidCustomer(int customerID) {
        String query = "SELECT COUNT(*) FROM tblCustomer WHERE CustomerID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to calculate how much of a discount the order was
     * @param Order order to check
     * @return double of how much of a discount it was
     */
    public static double calculateDiscount(Order order) {
        double regularTotal = 0.0;
        String query = "SELECT UnitPrice FROM tblProduct WHERE ProductID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Order.OrderItem item : order.getOrderItems()) {
                stmt.setString(1, item.getProductID());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double unitPrice = rs.getDouble("UnitPrice");
                    regularTotal += unitPrice * item.getQuantityOrdered();
                }
            }

            return regularTotal - order.calculateSubtotal();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
