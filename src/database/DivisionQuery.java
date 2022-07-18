package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**This class makes sql statements to obtain division data.
 *
 */
public class DivisionQuery {

    /**A Method to get all divisions
     * @return ObservableList returns a list of all divisions.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Division> getDivisions() throws SQLException {
        ObservableList<Division> divisions = FXCollections.observableArrayList();

        String divQuery = "Select * FROM first_level_divisions;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), divQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {

                Division newDivision = new Division(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID")
                );
                divisions.add(newDivision);
            }
            return divisions;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**A Method to get retrieve a division by its name
     * @param division division name
     * @return Division returns the division object.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static Division getDivisionId(String division) throws SQLException {
        String divQuery = "SELECT * FROM first_level_divisions WHERE Division=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), divQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, division);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Division newDiv = new Division(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID")
                );
                return newDiv;
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }


    /**A Method to get retrieve all divisions that belong a country
     * @param country country name
     * @return ObservableList returns all of that country's divisions
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Division> getDivisionsByCountry(String country) throws SQLException {
        Country newCountry = CountryQuery.getCountryId(country);

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        String divsFromCountryQuery = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID=?;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), divsFromCountryQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        assert newCountry != null;
        preparedStatement.setInt(1, newCountry.getCountryId());

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Division newDiv = new Division(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID")
                );
                divisions.add(newDiv);
            }
            return divisions;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }








}
