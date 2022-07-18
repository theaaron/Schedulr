package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//Lambda method used to print status. replaces system.out.println to give a cleaner look.

/**This interface allows us to use a lambda method.
 *
 */
interface ConnectionStatus {
    public void print(String str);
}

/**This Class connects the program to the database.
 *
 */
public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    private static Connection connection = null;  // Connection Interface
    private static PreparedStatement preparedStatement;

    /**Lambda method used to print status. replaces system.out.println to give a cleaner and more readable look.
     * takes in a string and prints the connection status.
     */
    static ConnectionStatus connectionStatus = (str) -> {
        System.out.println(str);
    };
    /**A Method to get make the connection to the database. Uses a lambda expression to print status.
     *
     */
     public static Connection makeConnection() {

          try {
              Class.forName(driver); // Locate Driver
              //password = Details.getPassword(); // Assign password
              connection = DriverManager.getConnection(jdbcUrl, userName, password); // reference Connection object
//              System.out.println("Connection successful!");
              connectionStatus.print("Connection Successful");

          }
          catch(ClassNotFoundException e) {
              connectionStatus.print("Error:" + e.getMessage());
          }
          catch(SQLException e) {
              connectionStatus.print("Error:" + e.getMessage());
          }
          return connection;
      }

    public static Connection getConnection() {
        return connection;
    }
    /**Closes the connection to the database.
    *
     */
    public static void closeConnection() {
         try {
             connection.close();
//             System.out.println("Connection closed!");
             connectionStatus.print("Connection closed");
         } catch (SQLException e) {
            connectionStatus.print(e.getMessage());
         }
    }

   public static void makePreparedStatement(String sqlStatement, Connection conn) throws SQLException {
       if (conn != null)
           preparedStatement = conn.prepareStatement(sqlStatement);
       else
           connectionStatus.print("Prepared Statement Creation Failed!");
   }
   public static PreparedStatement getPreparedStatement() throws SQLException {
       if (preparedStatement != null)
           return preparedStatement;
       else connectionStatus.print("Null reference to Prepared Statement");
       return null;
   }



}