package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This class makes sql statements to obtain contact data.
 *
 */
public class ContactQuery {
    /**A Method to get all contacts
     * @return ObservableList returns a list of all contacts.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Contact> getContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        String getContactsQuery = "SELECT * FROM contacts";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getContactsQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Contact newContact = new Contact(
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Email"),
                        resultSet.getInt("Contact_ID")
                );

                contacts.add(newContact);
            }
            return contacts;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**A Method to get all contact by their name.
     * @param contactName Contact Name.
     * @return Contact returns a contact object.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static Contact getContactByName(String contactName) throws SQLException {
        String getContactQuery = "SELECT * FROM contacts WHERE Contact_Name=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getContactQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();
        preparedStatement.setString(1, contactName);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Contact newContact = new Contact(
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Email"),
                        resultSet.getInt("Contact_ID")
                );
                return newContact;
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;

    }









}

