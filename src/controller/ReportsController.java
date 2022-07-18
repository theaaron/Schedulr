package controller;

import database.AppointmentQuery;
import database.ContactQuery;
import database.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**Controller for viewing reports.
 *
 */
public class ReportsController implements Initializable {
    public ComboBox<String> monthCombo;
    public ComboBox<String> typeCombo;
    public Button mainScreenBtn;
    public ComboBox<String> contactCombo;
    public ComboBox<Integer> custyCombo;
    public Button getApptsBtn;
    public TextField custyLabel;
    public TableView<Appointment> contactApptTable;
    public TableColumn contactApptIdCol;
    public TableColumn contactTitleCol;
    public TableColumn contactTypeCol;
    public TableColumn contactDescCol;
    public TableColumn contactStartCol;
    public TableColumn contactEndCol;
    public TableColumn contactCustIdCol;
    public TableView<Appointment> custApptTable;
    public TableColumn custApptIdCol;
    public TableColumn custTitleCol;
    public TableColumn custTypeCol;
    public TableColumn custDescCol;
    public TableColumn custStartCol;
    public TableColumn custEndCol;
    public TableColumn custContactId;
    public TextField monthTypeResults;

    /**Sends you back to the main screen.
     *
     */
    public void onMainScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**Method to update the table with all of a contact's appointments everytime a new contact is chosen.
     * @param actionEvent triggered by selecting a new contact.
     */

    public void onContactCombo(ActionEvent actionEvent) throws SQLException {
        String contactName = contactCombo.getSelectionModel().getSelectedItem();
        Contact selectedContact = ContactQuery.getContactByName(contactName);

        ObservableList<Appointment> contactAppts = FXCollections.observableArrayList();
        contactAppts = AppointmentQuery.getApptsByContact(selectedContact.getId());

        contactApptTable.setItems(contactAppts);
        contactApptTable.refresh();

        contactApptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        contactStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        contactEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        contactCustIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));


    }
    /**Method to update a table with all of a customers appointments.
     * @param actionEvent triggered by selecting a customer from the combobox.
     */
    public void onCustyCombo(ActionEvent actionEvent) throws SQLException {
        Integer selectedCustyId = custyCombo.getValue();
        Customer selectedCusty = CustomerQuery.getCustById(selectedCustyId);
        assert selectedCusty != null;
        custyLabel.setText(selectedCusty.getName());

        ObservableList<Appointment> custyAppts = AppointmentQuery.getApptsByCust(selectedCustyId);

        custApptTable.setItems(custyAppts);
        custApptTable.refresh();

        custApptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        custTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        custDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        custStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        custEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        custContactId.setCellValueFactory(new PropertyValueFactory<>("contactId"));


    }
    /**Method for getting the number of all appointments by type and month.
     * @param actionEvent triggered by clicking the get button.
     */
    public void onGetApptsButton(ActionEvent actionEvent) throws SQLException {
        if (typeCombo.getSelectionModel().isEmpty() || monthCombo.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select the month and type.");
            alert.showAndWait();
        } else {

            ObservableList<Appointment> appts = AppointmentQuery.getApptsByType(typeCombo.getSelectionModel().getSelectedItem());
            ObservableList<Appointment> apptsByMonth = FXCollections.observableArrayList();

            assert appts != null;
            for (Appointment appt : appts) {

                System.out.println(appt.getStartDate().getMonth().name());
                if (appt.getStartDate().getMonth().name().equals(monthCombo.getSelectionModel().getSelectedItem())) {
                    apptsByMonth.add(appt);
                }
            }

            monthTypeResults.setText(String.valueOf(apptsByMonth.size()));
        }

    }


    /**Method to get all customers from the database.
     *
     */
    private void populateCustId() {
        ObservableList<Integer> custyIds = FXCollections.observableArrayList();
        try {
            ObservableList<Customer> custys = CustomerQuery.getCustomers();
            assert custys != null;
            for (Customer custy: custys) {
                custyIds.add(custy.getId());
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        custyCombo.setItems(custyIds);
    }


    public void populateMonthCombo() {
        ObservableList<String> months = FXCollections.observableArrayList();
        months.add("JANUARY");
        months.add("FEBRUARY");
        months.add("MARCH");
        months.add("APRIL");
        months.add("MAY");
        months.add("JUNE");
        months.add("JULY");
        months.add("AUGUST");
        months.add("SEPTEMBER");
        months.add("OCTOBER");
        months.add("NOVEMBER");
        months.add("DECEMBER");

        monthCombo.setItems(months);

    }
    public void populateTypeCombo() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll("Surgery", "Physical", "Covid Test", "Checkup", "Follow-up");
        typeCombo.setItems(types);
    }

    /**Method to get all contacts from the database and add them to a combobox.
     *
     */
    private void populateContacts() throws SQLException {
        ObservableList<String> contactsComboList = FXCollections.observableArrayList();

        try {
            ObservableList<Contact> contacts = ContactQuery.getContacts();
            assert contacts != null;
            for (Contact contact: contacts) {
                if (!contactsComboList.contains(contact.getName())) {
                    contactsComboList.add(contact.getName());
                }
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        contactCombo.setItems(contactsComboList);
    }



    /**Initializes the view. Sets the comboboxes.
     * @param resourceBundle
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCustId();
        try {
            populateContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateMonthCombo();
        populateTypeCombo();
    }
}
