package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerQuery {

    /**A Method to get all customers.
     * @return ObservableList returns a list of all customers.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Customer> getCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String searchCustomers = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), searchCustomers);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Customer newCustomer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division"),
                        resultSet.getString("Country"),
                        resultSet.getInt("Division_ID")
                );
                customers.add(newCustomer);
            }
            return customers;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    /**A Method to create a new customer
     * @param name customer name
     * @param addy customer address
     * @param postal customer postal code
     * @param phone Customer phone number.
     * @param division Customer division.
     * @return boolean true if customer was successfully added.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static boolean addNewCustomer(String name, String addy, String postal, String phone, String division) throws SQLException {
        Division newDivision = DivisionQuery.getDivisionId(division);

        String addNewStatement = "INSERT INTO customers(Customer_Name, Address, Postal_code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), addNewStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, addy);
        preparedStatement.setString(3, postal);
        preparedStatement.setString(4, phone);
        assert newDivision != null;
        preparedStatement.setInt(5, newDivision.getDivisionId());

        try {
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows Changed: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No rows changed.");
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
    /**A Method to update a customer
     * @param name customer name
     * @param addy customer address
     * @param postal customer postal code
     * @param phone Customer phone number.
     * @param division Customer division.
     * @return boolean true if customer was successfully updated.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static boolean editCustomer(int id, String name, String addy, String postal, String phone, String division) throws SQLException {

        Division newDiv = DivisionQuery.getDivisionId(division);

        String editStatement = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? WHERE Customer_ID=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), editStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, addy);
        preparedStatement.setString(3, postal);
        preparedStatement.setString(4, phone);
        assert newDiv != null;
        preparedStatement.setInt(5, newDiv.getDivisionId());
        preparedStatement.setInt(6, id);

        try {
            preparedStatement.execute();

            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows edited: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No rows edited.");
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**A Method to delete a customer
     * @param id customer ID
     * @return boolean true if customer was successfully deleted.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static boolean delCustomer(int id) throws SQLException {
        String delStatement = "DELETE from customers WHERE Customer_ID=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), delStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, id);

        try {
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows deleted: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No rows deleted");
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return false;
    }
    /**A Method to get a customer by its ID.
     * @param id Customer ID.
     * @return Customer the customer object.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static Customer getCustById(Integer id) throws SQLException {
        ObservableList<Customer> custys = getCustomers();

        try {
            assert custys != null;
            for (Customer custy: custys) {
                if( custy.getId() == id) {
                    return custy;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }













}
