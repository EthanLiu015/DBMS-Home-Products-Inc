# Home Products Inc. - Database Management System

A full-stack desktop business management application built from the ground up in Java. This system provides a robust, multi-screen GUI to perform full CRUD operations on customers, orders, products, and more, all powered by a MySQL database.

**Link to project:** http://recruiters-love-seeing-live-demos.com/

![A screenshot of the Home Products Inc. application main menu](http://placecorgi.com/1200/650)

## How to Run

### Prerequisites
*   Java Development Kit (JDK) 8 or higher.
*   MySQL Server running locally.
*   The MySQL Connector/J JAR file (e.g., `mysql-connector-j-9.1.0.jar`) placed in the root directory of the project.

### 1. Database Setup
1.  Create a new database in MySQL named `HomeProductsIncSmall`.
2.  Run the provided SQL schema script to create the necessary tables and seed initial data.
3.  Update the `DatabaseConfig.java` file with your local MySQL username and password:
    ```java
    // in DatabaseConfig.java
    private static final String DB_USERNAME = "your_mysql_username";
    private static final String DB_PASSWORD = "your_mysql_password";
    ```

### 2. Compilation
Open a terminal or command prompt in the project's root directory and run the following command. Make sure to replace `mysql-connector-j-9.1.0.jar` with the actual name of your MySQL connector file.

```bash
javac -cp .:mysql-connector-j-9.1.0.jar -d build/classes *.java
```
This will compile all `.java` files and place the `.class` files into the `build/classes` directory.

### 3. Running the Application
Once compiled, run the application with the following command:

```bash
java -cp build/classes:mysql-connector-j-9.1.0.jar MainApplication
```
The main menu of the application should now appear.

## How It's Made:

**Tech used:** Java, Java Swing, JDBC, MySQL

This application was built using a layered architecture to separate concerns and improve maintainability, with Java Swing for the front-end, a service layer for business logic, and JDBC for database communication.

*   **Front-End (View):** The user interface is built entirely with **Java Swing**, featuring a `CardLayout` to manage navigation between multiple screens. A central `UIFactory` class was implemented to create consistent, styled components (buttons, text fields, headers) across the application, demonstrating the Factory design pattern to reduce code duplication and enforce a uniform look and feel.

*   **Back-End (Controller/Service):** A dedicated service layer (`CustomerService`, `OrderService`, etc.) handles all business logic. This layer acts as an intermediary between the UI and the database, processing data and performing validation before executing database operations.

*   **Database (Model):** All data is stored in a **MySQL** database. The application uses **JDBC** for all database interactions, including complex transactional operations. For example, the order creation process is wrapped in a transaction to ensure that an order and all its line items are saved together, or the entire operation is rolled back on failure, guaranteeing data integrity.

*   **Data Validation:** Robust, real-time validation is implemented on both the front-end (e.g., limiting input length, enforcing character formats with `DocumentFilter` and `MaskFormatter`) and the back-end (e.g., checking for the existence of a customer or product before saving).

## Optimizations
*(optional)*

Throughout development, several key optimizations and refactoring steps were taken to improve code quality and performance.

*   **Centralized Validation Logic:** Initially, some validation checks (like verifying a customer's existence) were duplicated across different service classes. This was refactored into a single, authoritative method within `CustomerService` and called from all necessary locations, adhering to the Don't Repeat Yourself (DRY) principle.

*   **Transactional Integrity:** The `OrderService.addOrder` method was built to be fully transactional. By disabling auto-commit, the application can execute multiple database inserts for the order header and its associated products as a single atomic unit. If any part fails, the entire transaction is rolled back, preventing partial or corrupt data from being saved to the database.

*   **Future Improvement - Connection Pooling:** Currently, the application opens a new database connection for each query. A major performance optimization would be to implement a JDBC connection pool (like HikariCP) to reuse existing connections, significantly reducing the overhead of database communication.

## Lessons Learned:

No matter what your experience level, being an engineer means continuously learning. Every time you build something you always have those *whoa this is awesome* or *wow I actually did it!* moments. This is where you should share those moments! Recruiters and interviewers love to see that you're self-aware and passionate about growing.

Building this project from scratch was a fantastic learning experience that solidified core software engineering principles.

*   **The Power of a Service Layer:** The most significant lesson was the importance of separating business logic from the UI. Having a dedicated service layer made the code much cleaner, easier to debug, and more scalable. When a validation rule changed (like the customer credit limit), I only had to update it in one place (`CustomerService`) instead of hunting through UI code.

*   **User-Centric Design:** Early versions of the UI had generic error messages and minor layout issues. Through iterative improvements, I learned how crucial specific feedback ("Credit limit cannot exceed $20,000" vs. "Save Error") and a polished, intuitive layout are for creating a usable application.

*   **Defensive Database Programming:** I learned firsthand why database transactions are critical. Debugging a `Duplicate entry` error when saving an order taught me to synchronize the UI state with the data model just before saving, and to wrap the entire multi-table insert operation in a transaction to ensure data consistency. It highlighted that what the user sees must perfectly match what is sent to the database.

## Examples:
Take a look at these couple examples that I have in my own portfolio:

**Palettable:** https://github.com/alecortega/palettable

**Twitter Battle:** https://github.com/alecortega/twitter-battle

**Patch Panel:** https://github.com/alecortega/patch-panel