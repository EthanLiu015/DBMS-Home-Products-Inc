public class Product
{
    private String ProductID; // varchar(4)
    private String ProductDescription; // varchar(200)
    private double UnitPrice; // decimal(10,2)
    private short UnitsonHand; // smallint
    private String ProductClass; // varchar(2)
    private int Warehouse; // int

    /**
     * Gets the unique identifier for the product.
     * @return the ProductID.
     */
    public String getProductID()
    {
        return ProductID;
    }

    /**
     * Sets the unique identifier for the product.
     * @param ProductID the ProductID to set.
     */
    public void setProductID(String ProductID)
    {
        this.ProductID = ProductID;
    }

    /**
     * Gets the description of the product.
     * @return the ProductDescription.
     */
    public String getProductDescription()
    {
        return ProductDescription;
    }

    /**
     * Sets the description of the product.
     * @param ProductDescription the ProductDescription to set.
     */
    public void setProductDescription(String ProductDescription)
    {
        this.ProductDescription = ProductDescription;
    }

    /**
     * Gets the unit price of the product.
     * @return the UnitPrice.
     */
    public double getUnitPrice()
    {
        return UnitPrice;
    }

    /**
     * Sets the unit price of the product.
     * @param UnitPrice the UnitPrice to set.
     */
    public void setUnitPrice(double UnitPrice)
    {
        this.UnitPrice = UnitPrice;
    }

    /**
     * Gets the number of units on hand.
     * @return the UnitsonHand.
     */
    public short getUnitsonHand()
    {
        return UnitsonHand;
    }

    /**
     * Sets the number of units on hand.
     * @param UnitsonHand the UnitsonHand to set.
     */
    public void setUnitsonHand(short UnitsonHand)
    {
        this.UnitsonHand = UnitsonHand;
    }

    /**
     * Gets the class of the product.
     * @return the ProductClass.
     */
    public String getProductClass()
    {
        return ProductClass;
    }

    /**
     * Sets the class of the product.
     * @param ProductClass the ProductClass to set.
     */
    public void setProductClass(String ProductClass)
    {
        this.ProductClass = ProductClass;
    }

    /**
     * Gets the warehouse number associated with the product.
     * @return the Warehouse.
     */
    public int getWarehouse()
    {
        return Warehouse;
    }

    /**
     * Sets the warehouse number associated with the product.
     * @param Warehouse the Warehouse to set.
     */
    public void setWarehouse(int Warehouse)
    {
        this.Warehouse = Warehouse;
    }

    /**
     * Returns a string representation of the Product object.
     * @return a string containing the details of the product.
     */
    @Override
    public String toString()
    {
        return "Product{" +
                "ProductID='" + ProductID + '\'' +
                ", ProductDescription='" + ProductDescription + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", UnitsonHand=" + UnitsonHand +
                ", ProductClass='" + ProductClass + '\'' +
                ", Warehouse=" + Warehouse +
                '}';
    }
}
