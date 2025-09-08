public final class DatabaseConfig {  
    //This is obviously not a great practice to store credentials in plaintext
    //These would usually be stored somewhere else in a secure manner and retrieved by the app
    private static final String DB_URL = "jdbc:mysql://localhost:3306/HomeProductsIncSmall"; //update with your port and DB name
    private static final String DB_USERNAME = "root";  //root should be the main username
    private static final String DB_PASSWORD = "3vilsquirrel"; //update with your password
  
    public static String getDbUrl() {  
        return DB_URL;
    }  
  
    public static String getDbUsername() {  
        return DB_USERNAME;
    }  
  
    public static String getDbPassword() {  
        return DB_PASSWORD;
    }  
}  
