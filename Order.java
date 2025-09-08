import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order {
    // Order details
    private int orderID;
    private int customerID;
    private Date orderDate;
    private Date shippingDate;
    private String status;
    private String shippingMethod;
    private double salesTax;

    // Customer details
    private String customerFirstName;
    private String customerLastName;

    // Lists for multiple items and payments
    private List<OrderItem> orderItems;
    private List<Payment> payments;

    // Constructor
    public Order() {
        this.orderItems = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    // Inner class for OrderItem remains the same
    public static class OrderItem {
        private String productID;
        private String productDescription;
        private int quantityOrdered;
        private double quotedPrice;

        // OrderItem getters and setters remain the same
        public String getProductID() { return productID; }
        public void setProductID(String productID) { this.productID = productID; }
        public String getProductDescription() { return productDescription; }
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
        public int getQuantityOrdered() { return quantityOrdered; }
        public void setQuantityOrdered(int quantityOrdered) { this.quantityOrdered = quantityOrdered; }
        public double getQuotedPrice() { return quotedPrice; }
        public void setQuotedPrice(double quotedPrice) { this.quotedPrice = quotedPrice; }
    }

    // Inner class for Payment
    public static class Payment {
        private int paymentID;
        private Date paymentDate;
        private String cardHolder;
        private double amount;
        private String paymentMethod;
        private String cardNumber;
        private Date expirationDate;
        private boolean isCreditCard;

        // Payment getters and setters
        public int getPaymentID() { return paymentID; }
        public void setPaymentID(int paymentID) { this.paymentID = paymentID; }
        public Date getPaymentDate() { return paymentDate; }
        public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
        public String getCardHolder() { return cardHolder; }
        public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
        public Date getExpirationDate() { return expirationDate; }
        public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
        public boolean getIsCreditCard() { return isCreditCard; }
        public void setIsCreditCard(boolean isCreditCard) { this.isCreditCard = isCreditCard; }
    }

    // Calculation methods remain the same
    public double calculateSubtotal() {
        return orderItems.stream()
                .mapToDouble(item -> item.getQuotedPrice())
                .sum();
    }

    public double calculateTax() {
        return calculateSubtotal() * (salesTax);
    }

    public double calculateTotal() {
        return calculateSubtotal() + calculateTax();
    }

    // List management methods
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    // Basic order field getters and setters
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public Date getShippingDate() { return shippingDate; }
    public void setShippingDate(Date shippingDate) { this.shippingDate = shippingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }
    public double getSalesTax() { return salesTax; }
    public void setSalesTax(double salesTax) { this.salesTax = salesTax; }

    // Customer detail getters and setters
    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }
    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

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
