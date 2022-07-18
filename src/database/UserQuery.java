package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**This class makes sql statements to obtain User data.
 *
 */
public class UserQuery {
    /**Verifies that the username and password are correct.
     * @param user username
     * @param pass password
     * @return boolean true if user and pass are in the database.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static boolean checkLoginInfo(String user, String pass) throws SQLException {
        String checkLoginQuery = "SELECT * FROM users WHERE User_Name=? AND Password=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), checkLoginQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, user);
        preparedStatement.setString(2, pass);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }
    /**A Method to get all users
     * @return ObservableList returns a list of all users.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<User> getUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();

        String getUsersStatement = "SELECT * FROM users;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), getUsersStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("User_Id"),
                        resultSet.getString("User_Name"),
                        resultSet.getString("Password")
                );
                users.add(newUser);
            }
            return users;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
