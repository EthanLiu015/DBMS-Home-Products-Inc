import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Payment
 * @author Ethan Liu
 * @author Matthew Carrieri
 * I have neither given nor received any unauthorized aid
 */
public class PaymentService {

    /**
     * method to add a payment
     * @param payment payment to add
     * @return boolean of if payment was added
     */
    public static boolean addPayment(Payment payment) {
        String query = "INSERT INTO tblPayment (CustomerID, OrderID, PaymentDate, Amount, Method, " +
                    "CardHolder, CardNumber, ExpirationDate, BooleanCreditCard) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, payment.getCustomerID());
            stmt.setInt(2, payment.getOrderID());
            stmt.setDate(3, payment.getPaymentDate());
            stmt.setDouble(4, payment.getAmount());
            stmt.setString(5, payment.getMethod());
            stmt.setString(6, payment.getCardHolder());
            stmt.setString(7, payment.getCardNumber());
            stmt.setDate(8, payment.getExpirationDate());
            stmt.setBoolean(9, payment.isCreditCard());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * method to get all orders are are unpaid
     * @param customerID customer to get all unpaid orders for
     * @return the list of unpaid orders
     */
    public static List<Order> getUnpaidOrders(int customerID) {
        List<Order> unpaidOrders = new ArrayList<>();
        String query = "SELECT o.*, c.FirstName, c.LastName FROM tblOrder o " +
                    "JOIN tblCustomer c ON o.CustomerID = c.CustomerID " +
                    "WHERE o.CustomerID = ? " +
                    "AND o.OrderStatus IN ('Partial', 'Unpaid')";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
                stmt.setInt(1, customerID);
                ResultSet rs = stmt.executeQuery();
            
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(rs.getInt("OrderID"));
                    order.setCustomerID(rs.getInt("CustomerID"));
                    order.setOrderDate(rs.getDate("OrderDate"));
                    order.setShippingDate(rs.getDate("ShippingDate"));
                    order.setStatus(rs.getString("OrderStatus"));
                    order.setShippingMethod(rs.getString("ShippingMethod"));
                    order.setSalesTax(rs.getDouble("SalesTax"));
                    order.setCustomerFirstName(rs.getString("FirstName"));
                    order.setCustomerLastName(rs.getString("LastName"));
                
                    loadOrderItems(order);
                    loadOrderPayments(order);
                    
                    unpaidOrders.add(order);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return unpaidOrders;
    }

    /**
     * helper method to load all the products in an order
     * @param order order to load products for
     */
    private static void loadOrderItems(Order order) {
        String query = "SELECT op.*, p.ProductDescription " + 
                " FROM tblOrdersProducts op JOIN tblProduct p ON op.ProductID = p.ProductID WHERE OrderID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                    DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
                 PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, order.getOrderID());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order.OrderItem item = new Order.OrderItem();
                item.setProductID(rs.getString("ProductID"));
                item.setProductDescription(rs.getString("ProductDescription"));
                item.setQuantityOrdered(rs.getInt("QuantityOrdered"));
                item.setQuotedPrice(rs.getDouble("QuotedPrice"));
                
                order.addOrderItem(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * helper method to load all the payments in an order
     * @param order order to load products for
     */
    private static void loadOrderPayments(Order order) {
        String query = "SELECT * FROM tblPayment WHERE OrderID = ?";
        
         try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                    DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
                 PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, order.getOrderID());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order.Payment payment = new Order.Payment();
                payment.setPaymentID(rs.getInt("PaymentID"));
                payment.setPaymentDate(rs.getDate("PaymentDate"));
                payment.setAmount(rs.getDouble("Amount"));
                payment.setPaymentMethod(rs.getString("Method"));
                payment.setCardHolder(rs.getString("CardHolder"));
                payment.setCardNumber(rs.getString("CardNumber"));
                payment.setExpirationDate(rs.getDate("ExpirationDate"));
                payment.setIsCreditCard(rs.getBoolean("BooleanCreditCard"));
                
                order.addPayment(payment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
