import java.sql.*;
import java.util.*;

/**
 * Service class for Product
 * @author Ethan Liu
 * @author Matthew Carrieri
 * I have neither given nor received any unauthorized aid
 */
public class ProductService {
    /**
     * method to get all products
     * @return a list of all products
     */
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        
        String query = "SELECT * FROM tblProduct " +
            "ORDER BY ProductID";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getString("ProductID"));
                product.setProductDescription(rs.getString("ProductDescription"));
                product.setUnitPrice(rs.getDouble("UnitPrice"));
                product.setUnitsonHand(rs.getShort("UnitsOnHand"));
                product.setProductClass(rs.getString("Class"));
                product.setWarehouse(rs.getInt("WarehouseID"));
                products.add(product);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    /**
     * method to add a product
     * @param product product to add
     * @return boolean of whether product was added or not
     */
    public static boolean addProduct(Product product) {
        String query = "INSERT INTO tblProduct (ProductID, ProductDescription, UnitPrice, " +
            "UnitsOnHand, Class, WarehouseID) "+
            "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, product.getProductID());
            stmt.setString(2, product.getProductDescription());
            stmt.setDouble(3, product.getUnitPrice());
            stmt.setShort(4, product.getUnitsonHand());
            stmt.setString(5, product.getProductClass());
            stmt.setInt(6, product.getWarehouse());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to edit/update a product
     * @param product product to update
     * @return boolean of whether product was updated
     */
    public static boolean updateProduct(Product product) {
        String query = "UPDATE tblProduct SET ProductDescription = ?, UnitPrice = ?, UnitsOnHand = ?, " +
            "Class = ?, WarehouseID = ? " +
            "WHERE ProductID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, product.getProductDescription());
            stmt.setDouble(2, product.getUnitPrice());
            stmt.setShort(3, product.getUnitsonHand());
            stmt.setString(4, product.getProductClass());
            stmt.setInt(5, product.getWarehouse());
            stmt.setString(6, product.getProductID());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to check if a warehouse exists
     * @param warehouseId warehouseID to check
     * @return boolean of if the warehouse exists
     */
    public static boolean warehouseExists(int warehouseId) {
        String query = "SELECT COUNT(*) FROM tblWarehouse WHERE WarehouseID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to get all the warehouseIDs
     * @param warehouseId warehouseID to check
     * @return boolean of if the warehouse exists
     */
    public static List<Integer> getAllWarehouseIDs() {
        List<Integer> warehouseIDs = new ArrayList<>();
        String query = "SELECT WarehouseID FROM tblWarehouse ORDER BY WarehouseID";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                warehouseIDs.add(rs.getInt("WarehouseID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return warehouseIDs;
    }

    /**
     * method to check if a product exists
     * @param productID productID to check
     * @return boolean of if the product exists
     */
    public static boolean productIDExists(String productID) {
        String query = "SELECT COUNT(*) FROM tblProduct WHERE ProductID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to get a product's price
     * @param productId product to check
     * @return double of this price
     */
    public static double getProductPrice(String productId) {
        String query = "SELECT UnitPrice FROM tblProduct WHERE ProductID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("UnitPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
