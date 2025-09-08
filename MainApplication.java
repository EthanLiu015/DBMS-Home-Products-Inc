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
        SwingUtilities.invokeLater(() -> {
            mainFrame.setVisible(true);
            showScreen("MainMenu");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.start();
        });
    }

    private void addAndShowPanel(JPanel panel, String name) {
        SwingUtilities.invokeLater(() -> {
            cardPanel.add(panel, name);
            cardLayout.show(cardPanel, name);
        });
    }

    public void showCustomerDetails(Customer customer) {
        addAndShowPanel(new ViewAllCustomer(this, customer).getMainPanel(), "ViewAllCustomer");
    }

    public void showNewCustomer() {
        addAndShowPanel(new NewEditCustomer(this, null).getMainPanel(), "NewEditCustomer");
    }

    public void showEditCustomer(Customer customer) {
        addAndShowPanel(new NewEditCustomer(this, customer).getMainPanel(), "EditCustomer");
    }

    public void showOrderDetails(Order order) {
        addAndShowPanel(new ViewAllOrder(this, order).getMainPanel(), "ViewAllOrder");
    }

    public void showNewOrder() {
        addAndShowPanel(new NewEditOrder(this).getMainPanel(), "NewEditOrder");
    }

    public void showNewProduct() {
        addAndShowPanel(new NewEditProduct(this, null).getMainPanel(), "NewEditProduct");
    }

    public void showEditProduct(Product product) {
        addAndShowPanel(new NewEditProduct(this, product).getMainPanel(), "EditProduct");
    }

    public void showNewSalesRep() {
        addAndShowPanel(new NewEditSalesRep(this, null).getMainPanel(), "NewEditSalesRep");
    }

    public void showSalesRepDetails(SalesRep salesRep) {
        addAndShowPanel(new ViewAllSalesRep(this, salesRep).getMainPanel(), "ViewAllSalesRep");
    }

    public void showEditSalesRep(SalesRep salesRep) {
        addAndShowPanel(new NewEditSalesRep(this, salesRep).getMainPanel(), "EditSalesRep");
    }

}