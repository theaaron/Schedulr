package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/** This class is for creating and returning the prepared statement.
 *
 */
public class DatabaseQuery {
    private static PreparedStatement statement;

    /** This method sets the PreparedStatement object
     * @param connection database connection
     * @param sqlStatement Sql statement
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {
        statement = connection.prepareStatement(sqlStatement);
    }

    /** This method returns the Prepared Statement object
     * @return PreparedStatement
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }

}
