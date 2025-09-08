import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;  
import java.util.List;
import java.sql.Date;

public class HomeProductsRunner
{
	public static void main(String[] args)
	{
		// Test Case 1: Retrieve all customers
        List<Customer> customers = CustomerService.getAllCustomers();
        System.out.println("Test Case 1: Get All Customers - Total Customers Retrieved: " + customers.size());
        for (Customer customer : customers) {
            // System.out.println(customer);
        }
        
        // Create a sample customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setStreet("123 Elm Street");
        customer.setCity("Springfield");
        customer.setState("IL");
        customer.setZipCode("62704");
        customer.setCustomerCredit(1500.50);
        customer.setRepID(5);
        customer.setCompany("Doe Enterprises");
        customer.setWebsite("https://doeenterprises.com");
        customer.setCustomerEmail("john.doe@example.com");
        customer.setCustomerBusinessPhone("555-123-4567");
        customer.setCustomerCellPhone("555-765-4321");
        customer.setTitle("CEO");
        customer.setStatus("Active");
        customer.setNotes("Regular customer with excellent credit");
        
        // Attempt to add the customer
        boolean isAdded = CustomerService.addCustomer(customer);
        
        // Print result
        if (isAdded)
        {
            System.out.println("Customer added successfully!");
        }
        else
        {
            System.out.println("Failed to add customer.");
        }
	}
}