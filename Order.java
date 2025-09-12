import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order made by a customer. This class holds details about the order itself,
 * the customer who made it, and lists of associated products and payments.
 * @author Ethan Liu
 * @Version 1.0.0
 */
public class Order {
    // Order details
    private int orderID; // int
    private int customerID; // int
    private Date orderDate; // date
    private Date shippingDate; // date
    private String status; // varchar(50)
    private String shippingMethod; // varchar(50)
    private double salesTax; //decimal (10,2)

    // Customer details
    private String customerFirstName;
    private String customerLastName;

    // Lists for multiple items and payments
    private List<OrderItem> orderItems;
    private List<Payment> payments;

    /**
     * Constructs a new Order object and initializes the lists for order items and payments.
     */
    public Order() {
        this.orderItems = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    /**
     * Represents a single line item within an order, linking a product to the order.
     */
    public static class OrderItem {
        private String productID;
        private String productDescription;
        private int quantityOrdered;
        private double quotedPrice;

        /**
         * Gets the unique identifier for the product.
         * @return The product ID.
         */
        public String getProductID() { return productID; }

        /**
         * Sets the unique identifier for the product.
         * @param productID The product ID to set.
         */
        public void setProductID(String productID) { this.productID = productID; }

        /**
         * Gets the description of the product.
         * @return The product description.
         */
        public String getProductDescription() { return productDescription; }

        /**
         * Sets the description of the product.
         * @param productDescription The product description to set.
         */
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

        /**
         * Gets the quantity of the product ordered.
         * @return The quantity ordered.
         */
        public int getQuantityOrdered() { return quantityOrdered; }

        /**
         * Sets the quantity of the product ordered.
         * @param quantityOrdered The quantity ordered to set.
         */
        public void setQuantityOrdered(int quantityOrdered) { this.quantityOrdered = quantityOrdered; }

        /**
         * Gets the total quoted price for this line item (quantity * price).
         * @return The quoted price.
         */
        public double getQuotedPrice() { return quotedPrice; }

        /**
         * Sets the total quoted price for this line item.
         * @param quotedPrice The quoted price to set.
         */
        public void setQuotedPrice(double quotedPrice) { this.quotedPrice = quotedPrice; }
    }

    /**
     * Calculates the subtotal of the order by summing the quoted prices of all order items.
     * @return The order subtotal.
     */
    public double calculateSubtotal() {
        return orderItems.stream()
                .mapToDouble(item -> item.getQuotedPrice())
                .sum();
    }

    /**
     * Calculates the tax amount for the order based on the subtotal and sales tax rate.
     * @return The calculated tax amount.
     */
    public double calculateTax() {
        return calculateSubtotal() * (salesTax);
    }

    /**
     * Calculates the final total for the order, including subtotal and tax.
     * @return The final total amount.
     */
    public double calculateTotal() {
        return calculateSubtotal() + calculateTax();
    }

    /**
     * Adds an item to the order.
     * @param item The OrderItem to add.
     */
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }

    /**
     * Gets the list of all items in the order.
     * @return A list of OrderItems.
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * Adds a payment to the order.
     * @param payment The Payment to add.
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    /**
     * Gets the list of all payments associated with the order.
     * @return A list of Payments.
     */
    public List<Payment> getPayments() {
        return payments;
    }

    /**
     * Gets the unique identifier for the order.
     * @return The order ID.
     */
    public int getOrderID() { return orderID; }

    /**
     * Sets the unique identifier for the order.
     * @param orderID The order ID to set.
     */
    public void setOrderID(int orderID) { this.orderID = orderID; }

    /**
     * Gets the unique identifier for the customer who placed the order.
     * @return The customer ID.
     */
    public int getCustomerID() { return customerID; }

    /**
     * Sets the unique identifier for the customer who placed the order.
     * @param customerID The customer ID to set.
     */
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    
    /**
     * Gets the date the order was placed.
     * @return The order date.
     */
    public Date getOrderDate() { return orderDate; }
    /**
     * Sets the date the order was placed.
     * @param orderDate The order date to set.
     */
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    /**
     * Gets the date the order was shipped.
     * @return The shipping date.
     */
    public Date getShippingDate() { return shippingDate; }
    /**
     * Sets the date the order was shipped.
     * @param shippingDate The shipping date to set.
     */
    public void setShippingDate(Date shippingDate) { this.shippingDate = shippingDate; }
    /**
     * Gets the current status of the order (e.g., "Paid", "Unpaid").
     * @return The order status.
     */
    public String getStatus() { return status; }
    /**
     * Sets the current status of the order.
     * @param status The order status to set.
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Gets the shipping method for the order.
     * @return The shipping method.
     */
    public String getShippingMethod() { return shippingMethod; }
    /**
     * Sets the shipping method for the order.
     * @param shippingMethod The shipping method to set.
     */
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }
    /**
     * Gets the sales tax rate for the order.
     * @return The sales tax rate.
     */
    public double getSalesTax() { return salesTax; }
    /**
     * Sets the sales tax rate for the order.
     * @param salesTax The sales tax rate to set.
     */
    public void setSalesTax(double salesTax) { this.salesTax = salesTax; }

    /**
     * Gets the first name of the customer associated with the order.
     * @return The customer's first name.
     */
    public String getCustomerFirstName() { return customerFirstName; }
    /**
     * Sets the first name of the customer associated with the order.
     * @param customerFirstName The customer's first name to set.
     */
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }
    /**
     * Gets the last name of the customer associated with the order.
     * @return The customer's last name.
     */
    public String getCustomerLastName() { return customerLastName; }
    /**
     * Sets the last name of the customer associated with the order.
     * @param customerLastName The customer's last name to set.
     */
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

    /**
     * Returns a string representation of the Order object for debugging purposes.
     * @return A string containing key details of the order.
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", customerID=" + customerID +
                ", customerName='" + customerFirstName + " " + customerLastName + '\'' +
                ", orderDate=" + orderDate +
                ", shippingDate=" + shippingDate +
                ", status='" + status + '\'' +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", salesTax=" + salesTax +
                ", numberOfItems=" + orderItems.size() +
                ", numberOfPayments=" + payments.size() +
                ", total=" + calculateTotal() +
                '}';
    }
}
