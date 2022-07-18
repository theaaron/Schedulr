package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;

import javax.xml.crypto.Data;
import java.awt.geom.RectangularShape;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**This class makes sql statements to manipulate and get appointment data.
 *
 */
public class AppointmentQuery {

    /**This method gets a list of all Appointments
     * @return ObservableList returns all appointments
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Appointment> getAppointments() throws SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();

        String getApptsQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptsQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                appts.add(newAppt);
            }
            return appts;
        }
        catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return null;
        }
    }

    /**This method gets a list of all Appointments in the last thirty days.
     * @return ObservableList returns all appointments in the last thirty days.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Appointment> getApptsLastMonth() throws SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime monthAgo = today.minusDays(30);

        String getApptsQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Start < ? AND Start > ?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptsQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setDate(1, java.sql.Date.valueOf(today.toLocalDate()));
        preparedStatement.setDate(2, java.sql.Date.valueOf(monthAgo.toLocalDate()));

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                appts.add(newAppt);
            }
            return appts;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }


    }



    /**This method gets a list of all Appointments in the last seven days.
     * @return ObservableList returns all appointments in the last seven days.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Appointment> getApptsLastWeek() throws SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime weekAgo = today.minusDays(7);

        String getApptsQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Start < ? AND Start > ?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptsQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setDate(1, java.sql.Date.valueOf(today.toLocalDate()));
        preparedStatement.setDate(2, java.sql.Date.valueOf(weekAgo.toLocalDate()));

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                appts.add(newAppt);
            }
            return appts;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    /**This method adds an appointment to the database.
     * @param contactName appointment contact name
     * @param customerId appointment customer ID
     * @param desc appointment description.
     * @param end appointment end time.
     * @param location appointment location
     * @param start appointment start time.
     * @param title appointment title.
     * @param type appointment type.
     * @param userId appointment User ID.
     * @return boolean true if appointment was successfully added.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static boolean makeAppt(String contactName, String title, String desc, String location, String type, LocalDateTime start, LocalDateTime end, Integer customerId, Integer userId) throws SQLException {
        Contact contact = ContactQuery.getContactByName(contactName);

        String makeApptStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, Contact_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), makeApptStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, title);
        preparedStatement.setString(2, desc);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
        preparedStatement.setInt(7, customerId);
        preparedStatement.setInt(8, contact.getId());
        preparedStatement.setInt(9, userId);


        try {
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows Updated: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No rows updated.");
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**This method deletes an appointment from the database.
     * @param apptId appointment ID.
     * @return boolean true if appointment was successfully deleted.
     * @throws SQLException catches sqlexception and prints error messages.
     */

    public static boolean deleteAppt(int apptId) throws SQLException {
        String deleteStatement = "DELETE from appointments WHERE Appointment_ID=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), deleteStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, apptId);

        try {
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows updated: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No rows updated.");
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }


    /**This method updates an appointment.
     * @param contactName appointment contact name
     * @param custId appointment customer ID
     * @param desc appointment description
     * @param end appointment end time.
     * @param location appointment location
     * @param start appointment start time.
     * @param title appointment title.
     * @param type appointment type.
     * @param userId appointment User ID.
     * @return boolean true if appointment was successfully updated.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static boolean updateAppt(String contactName, String title, String desc, String location, String type, LocalDateTime start, LocalDateTime end, Integer custId, Integer userId, Integer apptId) throws SQLException {
        Contact contact = ContactQuery.getContactByName(contactName);

        String updateApptStatement = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Customer_ID=?, Contact_ID=?, User_ID=? WHERE Appointment_ID = ?;"; //get rid of sc

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), updateApptStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, title);
        preparedStatement.setString(2, desc);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4,type);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
        preparedStatement.setInt(7, custId);
        preparedStatement.setInt(8, contact.getId());
        preparedStatement.setInt(9, userId);
        preparedStatement.setInt(10, apptId);

        try {
            preparedStatement.execute();

            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows Updated: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No rows updated.");
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**This method gets a list of all of a customers Appointments
     * @param custId Customer ID.
     * @return ObservableList returns all appointments from a customer.
     * @throws SQLException catches sqlexception and prints error messages.
     */

    public static ObservableList<Appointment> getApptsByCust(int custId) throws  SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();

        String getApptsStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Customer_ID=?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptsStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, custId);

        try {
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                appts.add(newAppt);
            }
            return appts;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**This method gets a list of all of a contact's Appointments
     * @param contactId Contact ID
     * @return ObservableList returns all of contact's appointments
     * @throws SQLException catches sqlexception and prints error messages.
     */

    public static ObservableList<Appointment> getApptsByContact(int contactId) throws SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        String getApptsStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE a.Contact_ID=?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptsStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, contactId);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                appts.add(newAppt);
            }
            return appts;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    /**This method gets an appointment object by searching the appointment ID.
     * @param apptId Appointment ID.
     * @return Appointment returns the appointment
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static Appointment getApptById(int apptId) throws SQLException {
        String getApptStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Appointment_ID=?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, apptId);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                return newAppt;
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**This method gets a list of all Appointments by type.
     * @param apptType Appointment Type.
     * @return ObservableList returns all appointments of a certain type
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Appointment> getApptsByType(String apptType) throws  SQLException {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();

        String getApptsStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Type=?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getApptsStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, apptType);

        try {
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Appointment newAppt = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );
                appts.add(newAppt);
            }
            return appts;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }






















































}
