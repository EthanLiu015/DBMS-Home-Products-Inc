import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;  
import java.util.List;
import java.sql.Date;

/**
 * Service class for Customer
 * @author Ethan Liu
 * @author Matthew Carrieri
 * I have neither given nor received any unauthorized aid
 */
public class CustomerService
{
    /**
     * method to get all the customers
     * @param return a list of all customers
     */
    public static List<Customer> getAllCustomers()
    {
        List<Customer> customers = new ArrayList<>();  
        String query = "SELECT * FROM tblCustomer";  
  
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());  
             PreparedStatement preparedStatement = connection.prepareStatement(query);  
             ResultSet resultSet = preparedStatement.executeQuery())
        {
  
            while (resultSet.next())
            {
                // populate the customer
                Customer customer = new Customer();

                customer.setCustomerID(resultSet.getInt("CustomerID"));
                customer.setFirstName(resultSet.getString("FirstName"));
                customer.setLastName(resultSet.getString("LastName"));
                customer.setStreet(resultSet.getString("Address1"));
                customer.setCity(resultSet.getString("City"));
                customer.setState(resultSet.getString("State"));
                customer.setZipCode(resultSet.getString("ZipCode"));
                customer.setCustomerCredit(resultSet.getDouble("Credit"));
                customer.setRepID(resultSet.getInt("SalesRepID"));
                customer.setCompany(resultSet.getString("Company"));
                customer.setWebsite(resultSet.getString("Website"));
                customer.setCustomerEmail(resultSet.getString("Email"));
                customer.setCustomerBusinessPhone(resultSet.getString("BusinessNumber"));
                customer.setCustomerCellPhone(resultSet.getString("CellNumber"));
                customer.setTitle(resultSet.getString("Title"));
                customer.setStatus(resultSet.getString("CustomerStatus"));
                customer.setNotes(resultSet.getString("Notes"));
                
                customers.add(customer);  
            }  
        }
        catch (SQLException e)
        {
            e.printStackTrace();  
        }  
  
        return customers;  
    }  

    /**
     * method to add a customer to the database
     * @param customer is the customer to add
     * @return a boolean if the customer was successfully added
     */
    public static String addCustomer(Customer customer)
    {
        // As an example, let's say a new customer cannot have a credit limit over $20,000
        if (customer.getCustomerCredit() > 20000.00) {
            return "Credit limit cannot exceed $20,000.00.";
        }

        String query = "INSERT INTO tblCustomer (FirstName, LastName, Address1, City, State, ZipCode, Credit, SalesRepID, Company, Website, Email, BusinessNumber, CellNumber, Title, CustomerStatus, Notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getStreet());
            preparedStatement.setString(4, customer.getCity());
            preparedStatement.setString(5, customer.getState());
            preparedStatement.setString(6, customer.getZipCode());
            preparedStatement.setDouble(7, customer.getCustomerCredit());
            preparedStatement.setInt(8, customer.getRepID());
            preparedStatement.setString(9, customer.getCompany());
            preparedStatement.setString(10, customer.getWebsite());
            preparedStatement.setString(11, customer.getCustomerEmail());
            preparedStatement.setString(12, customer.getCustomerBusinessPhone());
            preparedStatement.setString(13, customer.getCustomerCellPhone());
            preparedStatement.setString(14, customer.getTitle());
            preparedStatement.setString(15, customer.getStatus());
            preparedStatement.setString(16, customer.getNotes());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
            {
                return null; // Success
            }
            return "Unable to save customer. Please check your data and try again.";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return "An error occurred while saving the customer: " + e.getMessage();
        }
    }

    /**
     * Query the database to check if a sales rep exists
     * @param repID is the id to check
     * @return true/false if the rep exists
     */
    public static boolean isValidSalesRep(int repID)
    {
        // query to run
        String query = "SELECT COUNT(*) FROM tblSalesRep WHERE SalesRepID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, repID);
            ResultSet rs = stmt.executeQuery();
            
            // if more than 1 sales rep exists return true
            if (rs.next())
            {
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return false;
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
     * update the parameters of the customer
     * @param customer to update
     * @return true/false if the update was successful
     */
    public static String updateCustomer(Customer customer)
    {
        // As an example, let's say a customer cannot have a credit limit over $20,000
        if (customer.getCustomerCredit() > 20000.00) {
            return "Credit limit cannot exceed $20,000.00.";
        }

        // query to run
        String query = "UPDATE tblCustomer SET " +
            "FirstName = ?, LastName = ?, Address1 = ?, City = ?, State = ?, ZipCode = ?, Credit = ?, " +
            "SalesRepID = ?, Company = ?, Website = ?, Email = ?, BusinessNumber = ?, CellNumber = ?, " +
            "Title = ?, CustomerStatus = ?, Notes = ? WHERE CustomerID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = connection.prepareStatement(query))
        {
        
            // set the new updates
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getStreet());
            stmt.setString(4, customer.getCity());
            stmt.setString(5, customer.getState());
            stmt.setString(6, customer.getZipCode());
            stmt.setDouble(7, customer.getCustomerCredit());
            stmt.setInt(8, customer.getRepID());
            stmt.setString(9, customer.getCompany());
            stmt.setString(10, customer.getWebsite());
            stmt.setString(11, customer.getCustomerEmail());
            stmt.setString(12, customer.getCustomerBusinessPhone());
            stmt.setString(13, customer.getCustomerCellPhone());
            stmt.setString(14, customer.getTitle());
            stmt.setString(15, customer.getStatus());
            stmt.setString(16, customer.getNotes());
            stmt.setInt(17, customer.getCustomerID());
            
            if (stmt.executeUpdate() > 0) {
                return null; // Success
            }
            return "Unable to update customer. Customer not found or data is unchanged.";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return "An error occurred while updating the customer: " + e.getMessage();
        }
    }

    /**
     * get a customer's order history
     * @param customerID is the customer of the order history to query
     * @return list of Orders
     */
    public static List<Order> getCustomerOrderHistory(int customerID)
    {
        List<Order> orderHistory = new ArrayList<>();

        // query to run
        String query = "SELECT * FROM tblOrder WHERE CustomerID = ? ORDER BY OrderDate DESC";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
            DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // set the values of the Order
            while (resultSet.next())
            {
                Order order = new Order();
                order.setOrderID(resultSet.getInt("OrderID"));
                order.setCustomerID(resultSet.getInt("CustomerID"));
                order.setOrderDate(resultSet.getDate("OrderDate"));
                order.setShippingDate(resultSet.getDate("ShippingDate"));
                order.setStatus(resultSet.getString("OrderStatus"));
                order.setShippingMethod(resultSet.getString("ShippingMethod"));
                order.setSalesTax(resultSet.getDouble("SalesTax"));
                
                orderHistory.add(order);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return orderHistory;
    }

    /**
     * get a customer's payment history
     * @param customerID is the customer of the payment history to query
     * @return list of payments
     */
    public static List<Payment> getCustomerPaymentHistory(int customerID)
    {
        List<Payment> paymentHistory = new ArrayList<>();

        // query to run
        String query = "SELECT * FROM tblPayment WHERE CustomerID = ? ORDER BY PaymentDate DESC";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
            DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // set the values of the payment
            while (resultSet.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(resultSet.getInt("PaymentID"));
                payment.setCustomerID(resultSet.getInt("CustomerID"));
                payment.setPaymentDate(resultSet.getDate("PaymentDate"));
                payment.setAmount(resultSet.getDouble("Amount"));
                payment.setMethod(resultSet.getString("Method"));
                
                paymentHistory.add(payment);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return paymentHistory;
    }

    /**
     * calculate the total spent on orders
     * @param customerID to calculate
     * @return double of accumulated sum of orders
     */
    public static double calculateCustomerTotalOrders(int customerID)
    {
        double totalOrders = 0.0;
        // query to run
        String query = "SELECT SUM(op.QuantityOrdered * op.QuotedPrice) as Total " +
                      "FROM tblOrdersProducts op " +
                      "JOIN tblOrder o ON o.OrderID = op.OrderID " +
                      "WHERE o.CustomerID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
            DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                totalOrders = rs.getDouble("Total");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return totalOrders;
    }

    /**
     * calculate the total spent through payments for an order
     * @param orderID to calculate
     * @return double of accumulated sum of money spent on an order
     */
    public static double calculateTotal(int orderID)
    {
        double total = 0.0;
        // query to run
        String query = "SELECT SUM(op.QuantityOrdered * op.QuotedPrice) as Subtotal, o.SalesTax " + 
                        "FROM tblOrdersProducts op " + 
                        "JOIN tblOrder o ON o.OrderID = op.OrderID " +
                        "WHERE o.OrderID = ? " +
                        "GROUP BY o.OrderID";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
            DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                double subtotal = rs.getDouble("Subtotal");
                double salesTax = rs.getDouble("SalesTax");
                total = subtotal + (subtotal * (salesTax));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return total;
    }

}