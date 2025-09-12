public class SalesRep
{
    private int RepID; // int
    private String LastName; // varchar(30)
    private String FirstName; // varchar(30)
    private String BusinessPhone; // varchar(15)
    private String CellPhone; // varchar(15)
    private String HomePhone; // varchar(15)
    private String FaxNumber; // varchar(15)
    private String Title; // varchar(50)
    private String Street; // varchar(50)
    private String City; // varchar(50)
    private String State; // varchar(2)
    private String ZipCode; // varchar(10)
    private double Commission; // decimal(10,2)
    private int Manager; // int

    /**
     * Gets the unique identifier for the sales representative.
     * @return the RepID.
     */
    public int getRepID()
    {
        return RepID;
    }

    /**
     * Sets the unique identifier for the sales representative.
     * @param RepID the RepID to set.
     */
    public void setRepID(int RepID)
    {
        this.RepID = RepID;
    }

    /**
     * Gets the last name of the sales representative.
     * @return the LastName.
     */
    public String getLastName()
    {
        return LastName;
    }

    /**
     * Sets the last name of the sales representative.
     * @param LastName the LastName to set.
     */
    public void setLastName(String LastName)
    {
        this.LastName = LastName;
    }

    /**
     * Gets the first name of the sales representative.
     * @return the FirstName.
     */
    public String getFirstName()
    {
        return FirstName;
    }

    /**
     * Sets the first name of the sales representative.
     * @param FirstName the FirstName to set.
     */
    public void setFirstName(String FirstName)
    {
        this.FirstName = FirstName;
    }

    /**
     * Gets the business phone number of the sales representative.
     * @return the BusinessPhone.
     */
    public String getBusinessPhone()
    {
        return BusinessPhone;
    }

    /**
     * Sets the business phone number of the sales representative.
     * @param BusinessPhone the BusinessPhone to set.
     */
    public void setBusinessPhone(String BusinessPhone)
    {
        this.BusinessPhone = BusinessPhone;
    }

    /**
     * Gets the cell phone number of the sales representative.
     * @return the CellPhone.
     */
    public String getCellPhone()
    {
        return CellPhone;
    }

    /**
     * Sets the cell phone number of the sales representative.
     * @param CellPhone the CellPhone to set.
     */
    public void setCellPhone(String CellPhone)
    {
        this.CellPhone = CellPhone;
    }

    /**
     * Gets the home phone number of the sales representative.
     * @return the HomePhone.
     */
    public String getHomePhone()
    {
        return HomePhone;
    }

    /**
     * Sets the home phone number of the sales representative.
     * @param HomePhone the HomePhone to set.
     */
    public void setHomePhone(String HomePhone)
    {
        this.HomePhone = HomePhone;
    }

    /**
     * Gets the fax number of the sales representative.
     * @return the FaxNumber.
     */
    public String getFaxNumber()
    {
        return FaxNumber;
    }

    /**
     * Sets the fax number of the sales representative.
     * @param FaxNumber the FaxNumber to set.
     */
    public void setFaxNumber(String FaxNumber)
    {
        this.FaxNumber = FaxNumber;
    }

    /**
     * Gets the title of the sales representative.
     * @return the Title.
     */
    public String getTitle()
    {
        return Title;
    }

    /**
     * Sets the title of the sales representative.
     * @param Title the Title to set.
     */
    public void setTitle(String Title)
    {
        this.Title = Title;
    }

    /**
     * Gets the street address of the sales representative.
     * @return the Street.
     */
    public String getStreet()
    {
        return Street;
    }

    /**
     * Sets the street address of the sales representative.
     * @param Street the Street to set.
     */
    public void setStreet(String Street)
    {
        this.Street = Street;
    }

    /**
     * Gets the city where the sales representative is located.
     * @return the City.
     */
    public String getCity()
    {
        return City;
    }

    /**
     * Sets the city where the sales representative is located.
     * @param City the City to set.
     */
    public void setCity(String City)
    {
        this.City = City;
    }

    /**
     * Gets the state where the sales representative is located.
     * @return the State.
     */
    public String getState()
    {
        return State;
    }

    /**
     * Sets the state where the sales representative is located.
     * @param State the State to set.
     */
    public void setState(String State)
    {
        this.State = State;
    }

    /**
     * Gets the zip code of the sales representative's location.
     * @return the ZipCode.
     */
    public String getZipCode()
    {
        return ZipCode;
    }

    /**
     * Sets the zip code of the sales representative's location.
     * @param ZipCode the ZipCode to set.
     */
    public void setZipCode(String ZipCode)
    {
        this.ZipCode = ZipCode;
    }

    /**
     * Gets the commission rate of the sales representative.
     * @return the Commission.
     */
    public double getCommission()
    {
        return Commission;
    }

    /**
     * Sets the commission rate of the sales representative.
     * @param Commission the Commission to set.
     */
    public void setCommission(double Commission)
    {
        this.Commission = Commission;
    }

    /**
     * Gets the manager ID associated with the sales representative.
     * @return the Manager.
     */
    public int getManager()
    {
        return Manager;
    }

    /**
     * Sets the manager ID associated with the sales representative.
     * @param Manager the Manager to set.
     */
    public void setManager(int Manager)
    {
        this.Manager = Manager;
    }

    /**
     * Returns a string representation of the SalesRep object.
     * @return a string containing the details of the sales representative.
     */
    @Override
    public String toString()
    {
        return "SalesRep{" +
                "RepID=" + RepID +
                ", LastName='" + LastName + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", BusinessPhone='" + BusinessPhone + '\'' +
                ", CellPhone='" + CellPhone + '\'' +
                ", HomePhone='" + HomePhone + '\'' +
                ", FaxNumber='" + FaxNumber + '\'' +
                ", Title='" + Title + '\'' +
                ", Street='" + Street + '\'' +
                ", City='" + City + '\'' +
                ", State='" + State + '\'' +
                ", ZipCode='" + ZipCode + '\'' +
                ", Commission=" + Commission +
                ", Manager=" + Manager +
                '}';
    }
}
