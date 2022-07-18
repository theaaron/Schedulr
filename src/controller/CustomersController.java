package controller;

import database.AppointmentQuery;
import database.CountryQuery;
import database.CustomerQuery;
import database.DivisionQuery;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**Controller for adding, editing and deleting customers, as well as navigating to other screens.
 *
 */
public class CustomersController implements Initializable {
    @FXML
    private TextField custIdField;
    @FXML
    private TextField custNameField;
    @FXML
    private TextField custAddyField;
    @FXML
    private TextField custPostField;
    @FXML
    private TextField custPhoneField;
    @FXML
    private ComboBox<String> custCountryField;
    @FXML
    private ComboBox<String> custDivisionField;
    @FXML
    private TableView<Customer> custTable;
    @FXML
    private TableColumn<Customer, Integer> custIdCol;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TableColumn<Customer, String> custAddressCol;
    @FXML
    private TableColumn<Customer, Integer> custPostCodeCol;
    @FXML
    private TableColumn<Customer, Integer> custPhoneCol;
    @FXML
    private TableColumn<Customer, Integer> custDivIdCol;
    @FXML
    private Button addCustBtn;
    @FXML
    private Button editCustBtn;
    @FXML
    private Button delCustBtn;
    @FXML
    private Button clearFormBtn;
    @FXML
    private Button apptsBtn;
    @FXML
    private Button reportsBtn;

    static ObservableList<Customer> customers;

    /**Validates that all inputs have data before adding to database.
     *
     */
    public boolean infoIsValid() {
        if (custNameField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter Name.");
            alert.showAndWait();
            return false;
        }
        if (custPhoneField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter Phone Number.");
            alert.showAndWait();
            return false;
        }
        if (custAddyField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter Address.");
            alert.showAndWait();
            return false;
        }
        if (custPostField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter Postal Code.");
            alert.showAndWait();
            return false;
        }
        if (custDivisionField.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter Division Info.");
            alert.showAndWait();
            return false;
        }
        if (custCountryField.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter Country.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**Method to verify that the selected customer has no appointments. You are unable to delete a customer if they have scheduled appointments.
     * @param custy the customer that needs to be checked
     * @return boolean returns true if customer is safe to delete.
     */
    public boolean hasNoAppts(Customer custy) {
        try {
            ObservableList appts = AppointmentQuery.getApptsByCust(custy.getId());
            if (appts != null && appts.size() < 1) {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**Adds customer to the database. Runs input validation checks before adding to database
     * @param actionEvent triggered by clicking the add button
     */

    public void onAddCust(ActionEvent actionEvent) throws SQLException {
        if (infoIsValid()) {
            String name = custNameField.getText();
            String addy = custAddyField.getText();
            String postal = custPostField.getText();
            String phone = custPhoneField.getText();
            String division = custDivisionField.getSelectionModel().getSelectedItem();
            System.out.println(custDivisionField.getSelectionModel().getSelectedItem());


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to add customer?");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.OK) {
                CustomerQuery.addNewCustomer(name, addy, postal, phone, division);
                System.out.println("add the custy");

                custTable.setItems(CustomerQuery.getCustomers());

                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Confirmed");
                confirm.setContentText("New Customer Added.");
                confirm.showAndWait();
            }
        }
    }

    /** Edits the selected customer. Runs validation checks then updates the database.
     * @param actionEvent triggered by clicking the update button.
     */

    public void onEditCust(ActionEvent actionEvent) throws SQLException {
        int id = Integer.parseInt(custIdField.getText());
        String name = custNameField.getText();
        String addy = custAddyField.getText();
        String postal = custPostField.getText();
        String phone = custPhoneField.getText();
        String division = custDivisionField.getSelectionModel().getSelectedItem();

        if (infoIsValid()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to update the selected customer?");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.OK) {
                //add to database
                CustomerQuery.editCustomer(id, name, addy, postal, phone, division);
                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Confirmed");
                confirm.setContentText("The Customer has been updated.");
                confirm.showAndWait();
            }
        }
        custTable.setItems(CustomerQuery.getCustomers());
    }

    /**Deletes the selected customer. Asks for confirmation then removes from the database.
     * @param actionEvent triggered by clicking the delete button.
     */
    public void onDelCust(ActionEvent actionEvent) throws SQLException {

        Customer selectedCusty = custTable.getSelectionModel().getSelectedItem();

        if (selectedCusty == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected customer?");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.OK) {
                try {
                    if (hasNoAppts(selectedCusty)) {
                        CustomerQuery.delCustomer(selectedCusty.getId());
                        custTable.setItems(CustomerQuery.getCustomers());
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setTitle("Confirmed");
                        confirm.setContentText("The Customer has been deleted.");
                        confirm.showAndWait();
                    } else {
                        Alert deleteError = new Alert(Alert.AlertType.ERROR);
                        deleteError.setTitle("Error");
                        deleteError.setContentText("Please delete all customer appointments before deleting customer.");
                        deleteError.showAndWait();
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**Clears all text in the text fields and comboboxes. Leaves the country selected to prevent an error from the division combobox.
     * @param actionEvent trigged by clicking the clear button.
     */
    public void onClearForm(ActionEvent actionEvent) {
        custNameField.setText("");
        custAddyField.setText("");
        custPostField.setText("");
        custIdField.setText("");
        custPhoneField.setText("");
        custDivisionField.setValue("");
//        custCountryField.setValue("");

    }
    /**Opens the Appointments View
     * @param actionEvent triggered by clicking the appointments button.
     */
    public void onAppts(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/AppointmentsView.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**Opens the Reports View
     * @param actionEvent triggered by clicking the reports button
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/ReportsView.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**Pulls countries from the database to load into the combobox.
     *
     */
    private void populateCountries() {
        ObservableList<String> listOfCountries = FXCollections.observableArrayList();

        try {
            ObservableList<Country> countries = CountryQuery.getCountries();
            if (countries != null) {
                for (Country country: countries) {
                    listOfCountries.add(country.getCountryName());
                    System.out.println(country.getCountryName());
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        custCountryField.setItems(listOfCountries);
    }
    /**Pulls divisions from the database to load into the combobos
     *
     */
    private void populateDivisions() {
        ObservableList<String> listOfDivs = FXCollections.observableArrayList();
        if (custCountryField.getSelectionModel().getSelectedItem() != null) {
            try {
                ObservableList<Division> divisions = DivisionQuery.getDivisionsByCountry(custCountryField.getSelectionModel().getSelectedItem());
                if (divisions != null) {
                    for (Division division : divisions) {
                        listOfDivs.add(division.getDivisionName());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ObservableList<Division> divisions = DivisionQuery.getDivisions();
                if (divisions != null) {
                    for (Division division: divisions) {
                        listOfDivs.add(division.getDivisionName());
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        custDivisionField.setItems(listOfDivs);
    }

    /**Initializes the view. sets up the table and comboboxes.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCountries();
        populateDivisions();

        try {
            customers = CustomerQuery.getCustomers();
            custTable.setItems(customers);
            custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            custAddressCol.setCellValueFactory(new PropertyValueFactory<>("addy"));
            custPostCodeCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
            custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            custDivIdCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**Loads the data from the selected customer into the text fields.
     * @param mouseEvent triggered by selecting a customer.
     */
    public void onCustySelected(MouseEvent mouseEvent) {
        if (!custTable.getSelectionModel().isEmpty()) {
            Customer selectedCusty = custTable.getSelectionModel().getSelectedItem();
            custNameField.setText(selectedCusty.getName());
            custAddyField.setText(selectedCusty.getAddy());
            custPostField.setText(selectedCusty.getPostal());
            custDivisionField.setValue(selectedCusty.getDivision());
            custCountryField.setValue(selectedCusty.getCountry());
            custPhoneField.setText(selectedCusty.getPhone());
            custIdField.setText(String.valueOf(selectedCusty.getId()));
            System.out.println("yes");
        }
    }

    public void onCountrySelected(ActionEvent actionEvent) {
        populateDivisions();
    }
}
