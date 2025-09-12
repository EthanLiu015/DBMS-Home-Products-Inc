public class Customer
{
    private int CustomerID; // int
    private String FirstName; // varchar(30)
    private String LastName; // varchar(30)
    private String Street; // varchar(50)
    private String City; // varchar(50)
    private String State; // varchar(2)
    private String ZipCode; // varchar(5)
    private double CustomerCredit; // decimal(10,2)
    private int RepID; // int
    private String Company; // varchar(100)
    private String Website; //  varchar(200)
    private String CustomerEmail; // varchar(100)
    private String CustomerBusinessPhone; // varchar(15)
    private String CustomerCellPhone; // varchar(15)
    private String Title; // varchar(50)
    private String Status; // varchar(10)
    private String Notes; // varchar(1000)

    /**
     * Gets the unique identifier for the customer.
     * @return the CustomerID.
     */
    public int getCustomerID()
    {
        return CustomerID;
    }

    /**
     * Sets the unique identifier for the customer.
     * @param CustomerID the CustomerID to set.
     */
    public void setCustomerID(int CustomerID)
    {
        this.CustomerID = CustomerID;
    }

    /**
     * Gets the first name of the customer.
     * @return the FirstName.
     */
    public String getFirstName()
    {
        return FirstName;
    }

    /**
     * Sets the first name of the customer.
     * @param FirstName the FirstName to set.
     */
    public void setFirstName(String FirstName)
    {
        this.FirstName = FirstName;
    }

    /**
     * Gets the last name of the customer.
     * @return the LastName.
     */
    public String getLastName()
    {
        return LastName;
    }

    /**
     * Sets the last name of the customer.
     * @param LastName the LastName to set.
     */
    public void setLastName(String LastName)
    {
        this.LastName = LastName;
    }

    /**
     * Gets the street address of the customer.
     * @return the Street.
     */
    public String getStreet()
    {
        return Street;
    }

    /**
     * Sets the street address of the customer.
     * @param Street the Street to set.
     */
    public void setStreet(String Street)
    {
        this.Street = Street;
    }

    /**
     * Gets the city of the customer.
     * @return the City.
     */
    public String getCity()
    {
        return City;
    }

    /**
     * Sets the city of the customer.
     * @param City the City to set.
     */
    public void setCity(String City)
    {
        this.City = City;
    }

    /**
     * Gets the state of the customer.
     * @return the State.
     */
    public String getState()
    {
        return State;
    }

    /**
     * Sets the state of the customer.
     * @param State the State to set.
     */
    public void setState(String State)
    {
        this.State = State;
    }

    /**
     * Gets the zip code of the customer.
     * @return the ZipCode.
     */
    public String getZipCode()
    {
        return ZipCode;
    }

    /**
     * Sets the zip code of the customer.
     * @param ZipCode the ZipCode to set.
     */
    public void setZipCode(String ZipCode)
    {
        this.ZipCode = ZipCode;
    }

    /**
     * Gets the customer credit.
     * @return the CustomerCredit.
     */
    public double getCustomerCredit()
    {
        return CustomerCredit;
    }

    /**
     * Sets the customer credit.
     * @param CustomerCredit the CustomerCredit to set.
     */
    public void setCustomerCredit(double CustomerCredit)
    {
        this.CustomerCredit = CustomerCredit;
    }

    /**
     * Gets the representative ID associated with the customer.
     * @return the RepID.
     */
    public int getRepID()
    {
        return RepID;
    }

    /**
     * Sets the representative ID associated with the customer.
     * @param RepID the RepID to set.
     */
    public void setRepID(int RepID)
    {
        this.RepID = RepID;
    }

    /**
     * Gets the company of the customer.
     * @return the Company.
     */
    public String getCompany()
    {
        return Company;
    }

    /**
     * Sets the company of the customer.
     * @param Company the Company to set.
     */
    public void setCompany(String Company)
    {
        this.Company = Company;
    }

    /**
     * Gets the website of the customer.
     * @return the website.
     */
    public String getWebsite()
    {
        return Website;
    }

    /**
     * Sets the website of the customer.
     * @param Website the Website to set.
     */
    public void setWebsite(String Website)
    {
        this.Website = Website;
    }


    /**
     * Gets the email of the customer.
     * @return the CustomerEmail.
     */
    public String getCustomerEmail()
    {
        return CustomerEmail;
    }

    /**
     * Sets the email of the customer.
     * @param CustomerEmail the CustomerEmail to set.
     */
    public void setCustomerEmail(String CustomerEmail)
    {
        this.CustomerEmail = CustomerEmail;
    }

    /**
     * Gets the business phone number of the customer.
     * @return the CustomerBusinessPhone.
     */
    public String getCustomerBusinessPhone()
    {
        return CustomerBusinessPhone;
    }

    /**
     * Sets the business phone number of the customer.
     * @param CustomerBusinessPhone the CustomerBusinessPhone to set.
     */
    public void setCustomerBusinessPhone(String CustomerBusinessPhone)
    {
        this.CustomerBusinessPhone = CustomerBusinessPhone;
    }

    /**
     * Gets the cell phone number of the customer.
     * @return the CustomerCellPhone.
     */
    public String getCustomerCellPhone()
    {
        return CustomerCellPhone;
    }

    /**
     * Sets the cell phone number of the customer.
     * @param CustomerCellPhone the CustomerCellPhone to set.
     */
    public void setCustomerCellPhone(String CustomerCellPhone)
    {
        this.CustomerCellPhone = CustomerCellPhone;
    }

    /**
     * Gets the title of the customer.
     * @return the Title.
     */
    public String getTitle()
    {
        return Title;
    }

    /**
     * Sets the title of the customer.
     * @param Title the Title to set.
     */
    public void setTitle(String Title)
    {
        this.Title = Title;
    }

    /**
     * Gets the status of the customer.
     * @return the Status.
     */
    public String getStatus()
    {
        return Status;
    }

    /**
     * Sets the status of the customer.
     * @param Status the Status to set.
     */
    public void setStatus(String Status)
    {
        this.Status = Status;
    }

    /**
     * Gets the notes associated with the customer.
     * @return the Notes.
     */
    public String getNotes()
    {
        return Notes;
    }

    /**
     * Sets the notes associated with the customer.
     * @param Notes the Notes to set.
     */
    public void setNotes(String Notes)
    {
        this.Notes = Notes;
    }

    /**
     * Returns a string representation of the Customer object.
     * @return a string containing all customer details.
     */
    @Override
    public String toString()
    {
        return "Customer{" +
                "CustomerID=" + CustomerID +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Street='" + Street + '\'' +
                ", City='" + City + '\'' +
                ", State='" + State + '\'' +
                ", ZipCode='" + ZipCode + '\'' +
                ", CustomerCredit=" + CustomerCredit +
                ", RepID=" + RepID +
                ", Company='" + Company + '\'' +
                ", Website='" + Website + '\'' +
                ", CustomerEmail='" + CustomerEmail + '\'' +
                ", CustomerBusinessPhone='" + CustomerBusinessPhone + '\'' +
                ", CustomerCellPhone='" + CustomerCellPhone + '\'' +
                ", Title='" + Title + '\'' +
                ", Status='" + Status + '\'' +
                ", Notes='" + Notes + '\'' +
                '}';
    }
}
