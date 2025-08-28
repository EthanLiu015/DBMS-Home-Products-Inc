import java.sql.Date;

public class Payment
{
    private int PaymentID; // int
    private int CustomerID; // int
    private int OrderID; // int
    private Date PaymentDate; // date
    private double Amount; // decimal(10,2)
    private String Method; // varchar(50)
    private String CardHolder; // varchar(100)
    private String CardNumber; // varchar(16)
    private Date ExpirationDate; // date
    private boolean CreditCard; // boolean (was varchar in MySQL)

    /**
     * Gets the unique identifier for the payment.
     * @return the PaymentID.
     */
    public int getPaymentID()
    {
        return PaymentID;
    }

    /**
     * Sets the unique identifier for the payment.
     * @param PaymentID the PaymentID to set.
     */
    public void setPaymentID(int PaymentID)
    {
        this.PaymentID = PaymentID;
    }

    /**
     * Gets the unique identifier for the customer associated with the payment.
     * @return the CustomerID.
     */
    public int getCustomerID()
    {
        return CustomerID;
    }

    /**
     * Sets the unique identifier for the customer associated with the payment.
     * @param CustomerID the CustomerID to set.
     */
    public void setCustomerID(int CustomerID)
    {
        this.CustomerID = CustomerID;
    }

    /**
     * Gets the unique identifier for the order associated with the payment.
     * @return the OrderID.
     */
    public int getOrderID()
    {
        return OrderID;
    }

    /**
     * Sets the unique identifier for the order associated with the payment.
     * @param OrderID the OrderID to set.
     */
    public void setOrderID(int OrderID)
    {
        this.OrderID = OrderID;
    }

    /**
     * Gets the date when the payment was made.
     * @return the PaymentDate.
     */
    public Date getPaymentDate()
    {
        return PaymentDate;
    }

    /**
     * Sets the date when the payment was made.
     * @param PaymentDate the PaymentDate to set.
     */
    public void setPaymentDate(Date PaymentDate)
    {
        this.PaymentDate = PaymentDate;
    }

    /**
     * Gets the amount of the payment.
     * @return the Amount.
     */
    public double getAmount()
    {
        return Amount;
    }

    /**
     * Sets the amount of the payment.
     * @param Amount the Amount to set.
     */
    public void setAmount(double Amount)
    {
        this.Amount = Amount;
    }

    /**
     * Gets the method of payment (e.g., "Credit Card", "Cash").
     * @return the Method.
     */
    public String getMethod()
    {
        return Method;
    }

    /**
     * Sets the method of payment (e.g., "Credit Card", "Cash").
     * @param Method the Method to set.
     */
    public void setMethod(String Method)
    {
        this.Method = Method;
    }

    /**
     * Gets the name of the cardholder for the payment.
     * @return the Cardholder.
     */
    public String getCardHolder()
    {
        return CardHolder;
    }

    /**
     * Sets the name of the cardholder for the payment.
     * @param CardHolder the Cardholder to set.
     */
    public void setCardHolder(String CardHolder)
    {
        this.CardHolder = CardHolder;
    }

    /**
     * Gets the card number used for the payment.
     * @return the CardNumber.
     */
    public String getCardNumber()
    {
        return CardNumber;
    }

    /**
     * Sets the card number used for the payment.
     * @param CardNumber the CardNumber to set.
     */
    public void setCardNumber(String CardNumber)
    {
        this.CardNumber = CardNumber;
    }

    /**
     * Gets the expiration date of the card used for the payment.
     * @return the ExpirationDate.
     */
    public Date getExpirationDate()
    {
        return ExpirationDate;
    }

    /**
     * Sets the expiration date of the card used for the payment.
     * @param ExpirationDate the ExpirationDate to set.
     */
    public void setExpirationDate(Date ExpirationDate)
    {
        this.ExpirationDate = ExpirationDate;
    }

    /**
     * Checks if the payment was made using a credit card.
     * @return true if the payment was made using a credit card, otherwise false.
     */
    public boolean isCreditCard()
    {
        return CreditCard;
    }

    /**
     * Sets whether the payment was made using a credit card.
     * @param CreditCard true if the payment was made using a credit card, otherwise false.
     */
    public void setCreditCard(boolean CreditCard)
    {
        this.CreditCard = CreditCard;
    }

    /**
     * Returns a string representation of the Payment object.
     * @return a string containing the details of the payment.
     */
    @Override
    public String toString()
    {
        return "Payment{" +
                "PaymentID=" + PaymentID +
                ", CustomerID=" + CustomerID +
                ", OrderID=" + OrderID +
                ", PaymentDate=" + PaymentDate +
                ", Amount=" + Amount +
                ", Method='" + Method + '\'' +
                ", CardHolder='" + CardHolder + '\'' +
                ", CardNumber='" + CardNumber + '\'' +
                ", ExpirationDate=" + ExpirationDate +
                ", CreditCard=" + CreditCard +
                '}';
    }
}
