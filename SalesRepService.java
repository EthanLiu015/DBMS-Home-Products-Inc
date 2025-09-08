import java.sql.*;
import java.util.*;

/**
 * Service class for SalesRep
 * @author Ethan Liu
 * @author Matthew Carrieri
 * I have neither given nor received any unauthorized aid
 */
public class SalesRepService {
    
    /**
     * method to get all the sales reps
     * @return list of all the sales reps
     */
    public static List<SalesRep> getAllSalesReps() {
        List<SalesRep> salesReps = new ArrayList<>();
        String query = "SELECT * FROM tblSalesRep ORDER BY SalesRepID";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SalesRep rep = new SalesRep();
                rep.setRepID(rs.getInt("SalesRepID"));
                rep.setLastName(rs.getString("LastName"));
                rep.setFirstName(rs.getString("FirstName"));
                rep.setBusinessPhone(rs.getString("BusinessNumber"));
                rep.setCellPhone(rs.getString("CellNumber"));
                rep.setHomePhone(rs.getString("HomeNumber"));
                rep.setFaxNumber(rs.getString("FaxNumber"));
                rep.setTitle(rs.getString("Title"));
                rep.setStreet(rs.getString("Address1"));
                rep.setCity(rs.getString("City"));
                rep.setState(rs.getString("State"));
                rep.setZipCode(rs.getString("ZipCode"));
                rep.setCommission(rs.getDouble("CommissionRate"));
                rep.setManager(rs.getInt("SalesRepManagerID"));
                
                salesReps.add(rep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesReps;
    }

    /**
     * method to add a sales rep
     * @param rep sales rep to add
     */
    public static boolean addSalesRep(SalesRep rep) {
        String query = "INSERT INTO tblSalesRep (LastName, FirstName, BusinessNumber, " +
            "CellNumber, HomeNumber, FaxNumber, Title, Address1, City, State, " +
            "ZipCode, CommissionRate, SalesRepManagerID) "+
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, rep.getLastName());
            stmt.setString(2, rep.getFirstName());
            stmt.setString(3, rep.getBusinessPhone());
            stmt.setString(4, rep.getCellPhone());
            stmt.setString(5, rep.getHomePhone());
            stmt.setString(6, rep.getFaxNumber());
            stmt.setString(7, rep.getTitle());
            stmt.setString(8, rep.getStreet());
            stmt.setString(9, rep.getCity());
            stmt.setString(10, rep.getState());
            stmt.setString(11, rep.getZipCode());
            stmt.setDouble(12, rep.getCommission());
            stmt.setInt(13, rep.getManager());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to update a sales rep
     * @param rep the sales rep to update
     * @return boolean of if sales rep was updated
     */
    public static boolean updateSalesRep(SalesRep rep) {
        String query = "UPDATE tblSalesRep SET LastName = ?, FirstName = ?, BusinessNumber = ?, " +
            "CellNumber = ?, HomeNumber = ?, FaxNumber = ?, Title = ?, Address1 = ?, City = ?, State = ?, " +
            "ZipCode = ?, CommissionRate = ?, SalesRepManagerID = ? " +
            "WHERE SalesRepID = ?";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, rep.getLastName());
            stmt.setString(2, rep.getFirstName());
            stmt.setString(3, rep.getBusinessPhone());
            stmt.setString(4, rep.getCellPhone());
            stmt.setString(5, rep.getHomePhone());
            stmt.setString(6, rep.getFaxNumber());
            stmt.setString(7, rep.getTitle());
            stmt.setString(8, rep.getStreet());
            stmt.setString(9, rep.getCity());
            stmt.setString(10, rep.getState());
            stmt.setString(11, rep.getZipCode());
            stmt.setDouble(12, rep.getCommission());
            stmt.setInt(13, rep.getManager());
            stmt.setInt(14, rep.getRepID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * method to check if a sales rep manager is valid
     * @param managerID sales rep to check
     * @return boolean of if the sales rep is valid
     */
    public static boolean isValidManager(int managerId) {
        String query = "SELECT COUNT(*) FROM tblSalesRep WHERE SalesRepID = ?";
        
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), 
                DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
