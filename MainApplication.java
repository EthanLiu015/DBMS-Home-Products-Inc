import javax.swing.*;
import java.awt.*;

public class MainApplication {
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MainMenu mainMenu;
    private CustomerPresentation customerPresentation;
    private ViewAllCustomer viewAllCustomer;
    private NewEditCustomer newEditCustomer;
    private OrderPresentation orderPresentation;
    private ViewAllOrder viewAllOrder;
    private NewEditOrder newEditOrder;
    private ProductPresentation productPresentation;
    private SalesRepPresentation salesRepPresentation;
    private ViewAllSalesRep viewAllSalesRep;
    private NewEditSalesRep newEditSalesRep;
    private NewEditPayment newEditPayment;

    public MainApplication() {
        mainFrame = new JFrame("Home Products Inc.");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        mainMenu = new MainMenu(this);
        customerPresentation = new CustomerPresentation(this);
        orderPresentation = new OrderPresentation(this);
        productPresentation = new ProductPresentation(this);
        salesRepPresentation = new SalesRepPresentation(this);
        newEditPayment = new NewEditPayment(this);

        
        cardPanel.add(mainMenu.getMainPanel(), "MainMenu");
        cardPanel.add(customerPresentation.getMainPanel(), "CustomerPresentation");
        cardPanel.add(orderPresentation.getMainPanel(), "OrderPresentation");
        cardPanel.add(productPresentation.getMainPanel(), "ProductPresentation");
        cardPanel.add(salesRepPresentation.getMainPanel(), "SalesRepPresentation");
        cardPanel.add(newEditPayment.getMainPanel(), "NewEditPayment");

        
        mainFrame.add(cardPanel);
        mainFrame.setLocationRelativeTo(null);
    }

    public void showScreen(String screenName) {
        if (screenName.equals("CustomerPresentation")) {
            customerPresentation.refreshCustomerTable();
        } else if (screenName.equals("OrderPresentation")) {
            orderPresentation.refreshTable();
        } else if (screenName.equals("ProductPresentation")) {
            productPresentation.refreshProductTable();
        } else if (screenName.equals("SalesRepPresentation")){
            salesRepPresentation.refreshSalesRepTable();
        }
        cardLayout.show(cardPanel, screenName);
    }

    public void start() {
        mainFrame.setVisible(true);
        showScreen("MainMenu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.start();
        });
    }

    // Add this method
    public void showCustomerDetails(Customer customer) {
        viewAllCustomer = new ViewAllCustomer(this, customer);
        cardPanel.add(viewAllCustomer.getMainPanel(), "ViewAllCustomer");
        showScreen("ViewAllCustomer");
    }

    // Add this method
    public void showNewCustomer() {
        newEditCustomer = new NewEditCustomer(this, null);
        cardPanel.add(newEditCustomer.getMainPanel(), "NewEditCustomer");
        showScreen("NewEditCustomer");
    }

    // delete maybe
    public void showEditCustomer(Customer customer) {
        newEditCustomer = new NewEditCustomer(this, customer);
        cardPanel.add(newEditCustomer.getMainPanel(), "EditCustomer");
        showScreen("EditCustomer");
    }

     // Add this method
    public void showOrderDetails(Order order) {
        viewAllOrder = new ViewAllOrder(this, order);
        cardPanel.add(viewAllOrder.getMainPanel(), "ViewAllOrder");
        showScreen("ViewAllOrder");
    }

    // Add this method
    public void showNewOrder() {
        newEditOrder = new NewEditOrder(this);
        cardPanel.add(newEditOrder.getMainPanel(), "NewEditOrder");
        showScreen("NewEditOrder");  // Fixed typo from "NewEditOrdder" to "NewEditOrder"
    }

    // Add these methods to MainApplication class
public void showNewProduct() {
    NewEditProduct newProduct = new NewEditProduct(this, null);
    cardPanel.add(newProduct.getMainPanel(), "NewEditProduct");
    showScreen("NewEditProduct");
}

public void showEditProduct(Product product) {
    NewEditProduct editProduct = new NewEditProduct(this, product);
    cardPanel.add(editProduct.getMainPanel(), "EditProduct");
    showScreen("EditProduct");
}

public void showNewSalesRep() {
    NewEditSalesRep newSalesRep = new NewEditSalesRep(this, null);
    cardPanel.add(newSalesRep.getMainPanel(), "NewEditSalesRep");
    showScreen("NewEditSalesRep");
}

public void showSalesRepDetails(SalesRep salesRep) {
    viewAllSalesRep = new ViewAllSalesRep(this, salesRep);
    cardPanel.add(viewAllSalesRep.getMainPanel(), "ViewAllSalesRep");
        showScreen("ViewAllSalesRep");
}

public void showEditSalesRep(SalesRep salesRep) {
    NewEditSalesRep editSalesRep = new NewEditSalesRep(this, salesRep);
    cardPanel.add(editSalesRep.getMainPanel(), "EditSalesRep");
    showScreen("EditSalesRep");
}

}