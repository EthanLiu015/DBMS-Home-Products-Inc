public class Warehouse
{
    private int WarehouseNumber; // int
    private String Address1; // varchar(100)
    private String Address2; // varchar(100)
    private String City; // varchar(50)
    private String State; // varchar(2)
    private String ZipCode; // varchar(10)
    private String GeneralPhoneNumber; // varchar(15)
    private String Supervisor; // varchar(100)
    private String SupervisorPhone; // varchar(15)
    private int Capacity; // int
    private short NumberOfEmployees; // smallint

    /**
     * Gets the unique identifier for the warehouse.
     * @return the WarehouseNumber.
     */
    public int getWarehouseNumber()
    {
        return WarehouseNumber;
    }

    /**
     * Sets the unique identifier for the warehouse.
     * @param WarehouseNumber the WarehouseNumber to set.
     */
    public void setWarehouseNumber(int WarehouseNumber)
    {
        this.WarehouseNumber = WarehouseNumber;
    }

    /**
     * Gets the first line of the warehouse address.
     * @return the Address1.
     */
    public String getAddress1()
    {
        return Address1;
    }

    /**
     * Sets the first line of the warehouse address.
     * @param Address1 the Address1 to set.
     */
    public void setAddress1(String Address1)
    {
        this.Address1 = Address1;
    }

    /**
     * Gets the second line of the warehouse address.
     * @return the Address2.
     */
    public String getAddress2()
    {
        return Address2;
    }

    /**
     * Sets the second line of the warehouse address.
     * @param Address2 the Address2 to set.
     */
    public void setAddress2(String Address2)
    {
        this.Address2 = Address2;
    }

    /**
     * Gets the city where the warehouse is located.
     * @return the City.
     */
    public String getCity()
    {
        return City;
    }

    /**
     * Sets the city where the warehouse is located.
     * @param City the City to set.
     */
    public void setCity(String City)
    {
        this.City = City;
    }

    /**
     * Gets the state where the warehouse is located.
     * @return the State.
     */
    public String getState()
    {
        return State;
    }

    /**
     * Sets the state where the warehouse is located.
     * @param State the State to set.
     */
    public void setState(String State)
    {
        this.State = State;
    }

    /**
     * Gets the zip code of the warehouse location.
     * @return the ZipCode.
     */
    public String getZipCode()
    {
        return ZipCode;
    }

    /**
     * Sets the zip code of the warehouse location.
     * @param ZipCode the ZipCode to set.
     */
    public void setZipCode(String ZipCode)
    {
        this.ZipCode = ZipCode;
    }

    /**
     * Gets the general phone number of the warehouse.
     * @return the GeneralPhoneNumber.
     */
    public String getGeneralPhoneNumber()
    {
        return GeneralPhoneNumber;
    }

    /**
     * Sets the general phone number of the warehouse.
     * @param GeneralPhoneNumber the GeneralPhoneNumber to set.
     */
    public void setGeneralPhoneNumber(String GeneralPhoneNumber)
    {
        this.GeneralPhoneNumber = GeneralPhoneNumber;
    }

    /**
     * Gets the name of the warehouse supervisor.
     * @return the Supervisor.
     */
    public String getSupervisor()
    {
        return Supervisor;
    }

    /**
     * Sets the name of the warehouse supervisor.
     * @param Supervisor the Supervisor to set.
     */
    public void setSupervisor(String Supervisor)
    {
        this.Supervisor = Supervisor;
    }

    /**
     * Gets the phone number of the warehouse supervisor.
     * @return the SupervisorPhone.
     */
    public String getSupervisorPhone()
    {
        return SupervisorPhone;
    }

    /**
     * Sets the phone number of the warehouse supervisor.
     * @param SupervisorPhone the SupervisorPhone to set.
     */
    public void setSupervisorPhone(String SupervisorPhone)
    {
        this.SupervisorPhone = SupervisorPhone;
    }

    /**
     * Gets the capacity of the warehouse.
     * @return the Capacity.
     */
    public int getCapacity()
    {
        return Capacity;
    }

    /**
     * Sets the capacity of the warehouse.
     * @param Capacity the Capacity to set.
     */
    public void setCapacity(int Capacity)
    {
        this.Capacity = Capacity;
    }

    /**
     * Gets the number of employees working in the warehouse.
     * @return the NumberOfEmployees.
     */
    public short getNumberOfEmployees()
    {
        return NumberOfEmployees;
    }

    /**
     * Sets the number of employees working in the warehouse.
     * @param NumberOfEmployees the NumberOfEmployees to set.
     */
    public void setNumberOfEmployees(short NumberOfEmployees)
    {
        this.NumberOfEmployees = NumberOfEmployees;
    }

    /**
     * Returns a string representation of the Warehouse object.
     * @return a string containing the details of the warehouse.
     */
    @Override
    public String toString()
    {
        return "Warehouse{" +
                "WarehouseNumber=" + WarehouseNumber +
                ", Address1='" + Address1 + '\'' +
                ", Address2='" + Address2 + '\'' +
                ", City='" + City + '\'' +
                ", State='" + State + '\'' +
                ", ZipCode='" + ZipCode + '\'' +
                ", GeneralPhoneNumber='" + GeneralPhoneNumber + '\'' +
                ", Supervisor='" + Supervisor + '\'' +
                ", SupervisorPhone='" + SupervisorPhone + '\'' +
                ", Capacity=" + Capacity +
                ", NumberOfEmployees=" + NumberOfEmployees +
                '}';
    }
}
