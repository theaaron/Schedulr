package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**A Class that makes SQL statements to retrieve country data.
 *
 */
public class CountryQuery {
    /**A Method to get all countries
     * @return ObservableList returns a list of all countries.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static ObservableList<Country> getCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();

        String selectCountriesQuery = "SELECT * FROM countries;";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), selectCountriesQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Country newCountry = new Country(
                        resultSet.getInt("Country_ID"),
                        resultSet.getString("Country")
                );
                countries.add(newCountry);
            }
            return countries;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }


    /**A Method to get retrieve a country by its name
     * @param country country name
     * @return Country returns the country object.
     * @throws SQLException catches sqlexception and prints error messages.
     */
    public static Country getCountryId(String country) throws SQLException {
        String countryQuery = "SELECT * FROM countries WHERE Country=?";

        DatabaseQuery.setPreparedStatement(JDBC.makeConnection(), countryQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, country);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Country newCountry = new Country(
                        resultSet.getInt("Country_ID"),
                        resultSet.getString("Country")
                );
                return newCountry;
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }












}
