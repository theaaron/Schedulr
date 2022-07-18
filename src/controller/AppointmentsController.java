package controller;

import database.AppointmentQuery;
import database.ContactQuery;
import database.CustomerQuery;
import database.UserQuery;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for adding, editing and deleting appointments.
 * @author Aaron
 */
public class AppointmentsController implements Initializable {
    public TableView<Appointment> apptTable;
    public TableColumn apptIdCol;
    public TableColumn titleCol;
    public TableColumn descCol;
    public TableColumn locationCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn custIdCol;
    public TextField apptId;
    public TextField apptTitle;
    public TextArea apptDesc;
    public TextField apptLocation;
    public TextField apptType;
    public TextField custId;
    public DatePicker startDateTime;
    public DatePicker endDateTime;
    public ComboBox<String> contactCombo;
    public ComboBox<Integer> userId;
    public Button addBtn;
    public Button delBtn;
    public Button updateBtn;
    public ComboBox custyCombo;
    public RadioButton allTgl;
    public ToggleGroup monthWeekTgl;
    public RadioButton monthlyTgl;
    public RadioButton weeklyTgl;
    public ComboBox<String> startTime;
    public ComboBox<String> endTime;
    public ComboBox<Integer> custIdCombo;
    public TextField customerTextField;
    public Button mainScreenBtn;
    
    static ObservableList<Appointment> appts;
    public ComboBox<String> typeComboBox;

    /**check all fields to make sure they contain and entry.
     * @return returns a boolean true if everything checks out
     */
    public boolean inputsAreValid() {
        System.out.println("checking");

        if (apptTitle.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter in an Appointment Title");
            alert.showAndWait();
            return false;
        } else if (apptDesc.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter in an Appointment Description");
            alert.showAndWait();
            return false;
        } else if (apptLocation.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter in an Appointment Location");
            alert.showAndWait();
            return false;
        } else if (contactCombo.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a Contact");
            alert.showAndWait();
            return false;
        } else if (typeComboBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please Enter in an Appointment Type");
            alert.showAndWait();
            return false;
        } else if (startDateTime.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a starting Appointment Date");
            alert.showAndWait();
            return false;
        } else if (startTime.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a starting Appointment Time");
            alert.showAndWait();
            return false;

        } else if (endDateTime.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select an ending Appointment Date");
            alert.showAndWait();
            return false;
        } else if (endTime.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select an ending Appointment Time");
            alert.showAndWait();
            return false;

        } else if (custIdCombo.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a customer ID");
            alert.showAndWait();
            return false;
        } else if (userId.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a User ID");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /** Method to verify that the start time is before the end time and that the appointment starts and ends on the same day.
     * @return returns a boolean true if everything checks out.
     */
    public boolean timeIsValid() {
        LocalDateTime start = LocalDateTime.of(startDateTime.getValue(), LocalTime.parse(startTime.getSelectionModel().getSelectedItem()));
        LocalDateTime end = LocalDateTime.of(endDateTime.getValue(), LocalTime.parse(endTime.getSelectionModel().getSelectedItem()));

        LocalTime userStartTime = start.toLocalTime();
        LocalTime userEndTime = end.toLocalTime();

        LocalDate startDate = startDateTime.getValue();
        LocalDate endDate = endDateTime.getValue();

        if (userEndTime.isBefore(userStartTime) || userEndTime.equals(userStartTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The ending time must be after the starting time");
            alert.showAndWait();
            return false;
        } else if (!startDate.equals(endDate)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The appointment must start and end on the same day.");
            alert.showAndWait();
            return false;
        }
        System.out.println("checked if time is valid");
        return true;
    }

    /** Method to verify that no appointment is scheduled while another appointment is ongoing.
     * @return  returns a boolean true if everything checks out.
     */
    public boolean timesDoNotOverlap() {
        Appointment thisAppt = apptTable.getSelectionModel().getSelectedItem();
        LocalDate dateStart = startDateTime.getValue();
        LocalDate dateEnd = endDateTime.getValue();

        LocalTime timeStart = LocalTime.parse(startTime.getSelectionModel().getSelectedItem());
        LocalTime timeEnd = LocalTime.parse(endTime.getSelectionModel().getSelectedItem());

        LocalDateTime chosenStart = dateStart.atTime(timeStart);
        LocalDateTime chosenEnd = dateEnd.atTime(timeEnd);

        Integer cId = custIdCombo.getSelectionModel().getSelectedItem();

        LocalDateTime apptStartToCheck;
        LocalDateTime apptEndToCheck;

        try {
            ObservableList<Appointment> appts = AppointmentQuery.getApptsByCust(cId);
            for (Appointment appt: appts) {
                if (thisAppt == null ||thisAppt.getId() != appt.getId()) {
                    apptStartToCheck = appt.getStartDate().atTime(appt.getStartTime().toLocalTime());
                    apptEndToCheck = appt.getEndDate().atTime(appt.getEndTime().toLocalTime());

                    if (apptStartToCheck.isAfter(chosenStart) && apptStartToCheck.isBefore(chosenEnd)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Appointment may not overlap with previously found Appointments");
                        alert.showAndWait();
                        return false;
                    } else if (apptEndToCheck.isAfter(chosenStart) && apptEndToCheck.isBefore(chosenEnd)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Appointment may not overlap with previously found Appointments");
                        alert.showAndWait();
                        return false;
                    } else if (apptStartToCheck.isEqual(chosenStart) || apptEndToCheck.isEqual(chosenEnd)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Appointment may not overlap with previously found Appointments");
                        alert.showAndWait();
                        return false;
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /** method to convert time to EST
     * @return returns the the time converted to EST.
     */
    private ZonedDateTime convertTimeToEst(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneId.of("America/New_York"));
    }

    public void showBusinessHourAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please Schedule Between Business Hours: 8:00 AM - 10:00 PM EST.");
        alert.showAndWait();
    }

    /**Method to verify that appointment is scheduled during business hours.
     * @return returns a boolean if everything checks out.
     */
    public boolean isDuringBusinessHours() {
        ZonedDateTime convertedStartDateTime = convertTimeToEst(LocalDateTime.of(startDateTime.getValue(), LocalTime.parse(startTime.getSelectionModel().getSelectedItem())));
        ZonedDateTime convertedEndDateTime = convertTimeToEst(LocalDateTime.of(endDateTime.getValue(), LocalTime.parse(endTime.getSelectionModel().getSelectedItem())));

        if (convertedStartDateTime.toLocalTime().isAfter(LocalTime.of(22, 0))) {
            showBusinessHourAlert();
            return false;
        }

        if (convertedEndDateTime.toLocalTime().isAfter(LocalTime.of(22, 0))) {
            showBusinessHourAlert();
            return false;
        }

        if (convertedStartDateTime.toLocalTime().isBefore(LocalTime.of(8, 0))) {
            showBusinessHourAlert();
            return false;
        }

        if (convertedEndDateTime.toLocalTime().isBefore(LocalTime.of(8, 0))) {
            showBusinessHourAlert();
            return false;
        }

        return true;
    }

    /**Method that runs all time and input checks, then adds appointment to database if everything checks out.
     * @param actionEvent clicking the add button triggers this method.
     */
    public void onAddAppt(ActionEvent actionEvent) throws SQLException {
        try {
            System.out.println("trying");
            if (inputsAreValid() && timeIsValid() && timesDoNotOverlap() && isDuringBusinessHours()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to add a new appointment?");
                Optional<ButtonType> answer = alert.showAndWait();
                if (answer.isPresent() && answer.get() == ButtonType.OK) {

                    String contact = contactCombo.getSelectionModel().getSelectedItem();
                    String title = apptTitle.getText();
                    String desc = apptDesc.getText();
                    String loc = apptLocation.getText();
                    String type = typeComboBox.getSelectionModel().getSelectedItem();
                    System.out.println("checking time");
                    LocalDateTime start = LocalDateTime.of(startDateTime.getValue(), LocalTime.parse(startTime.getSelectionModel().getSelectedItem()));
                    LocalDateTime end = LocalDateTime.of(endDateTime.getValue(), LocalTime.parse(endTime.getSelectionModel().getSelectedItem()));
                    System.out.println("time checked");
                    Integer cId = custIdCombo.getSelectionModel().getSelectedItem();
                    Integer uId = userId.getSelectionModel().getSelectedItem();

                    boolean newApptCreated = AppointmentQuery.makeAppt(contact, title, desc, loc, type, start, end, cId, uId);
                    if (newApptCreated) {
                        apptTable.setItems(AppointmentQuery.getAppointments());
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setTitle("Confirmed");
                        confirm.setContentText("New appointment added.");
                        confirm.showAndWait();
                    }
                }
            }
            else {
                System.out.println();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**Method to delete the selected appointment. Asks for confirmation before deleting the appointment.
     * @param actionEvent clicking the delete button triggers this method.
     */
    public void onDelBtn(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete the selected appointment?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            Appointment selectedAppt = apptTable.getSelectionModel().getSelectedItem();
            AppointmentQuery.deleteAppt(selectedAppt.getId());
            apptTable.setItems(AppointmentQuery.getAppointments());
        }
    }


    /**Method to update an appointment. Runs all time and input checks and then asks for confirmation to update the appointment.
     * @param actionEvent clicking the edit button triggers this method.
     */
    public void onUpdateBtn(ActionEvent actionEvent) throws SQLException {
        Appointment selectedAppt = apptTable.getSelectionModel().getSelectedItem();
        if (selectedAppt != null) {
            try {
                if (inputsAreValid() && timeIsValid() && timesDoNotOverlap() && isDuringBusinessHours()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update the selected appointment?");
                    Optional<ButtonType> answer = alert.showAndWait();

                    if (answer.isPresent() && answer.get() == ButtonType.OK) {
                        String contact = contactCombo.getSelectionModel().getSelectedItem();
                        String title = apptTitle.getText();
                        String desc = apptDesc.getText();
                        String loc = apptLocation.getText();
                        String type = typeComboBox.getSelectionModel().getSelectedItem();
                        LocalDateTime start = LocalDateTime.of(startDateTime.getValue(), LocalTime.parse(startTime.getSelectionModel().getSelectedItem()));
                        LocalDateTime end = LocalDateTime.of(endDateTime.getValue(), LocalTime.parse(endTime.getSelectionModel().getSelectedItem()));
                        Integer cId = custIdCombo.getSelectionModel().getSelectedItem();
                        Integer uId = userId.getSelectionModel().getSelectedItem();
                        Integer aId = selectedAppt.getId();


                        AppointmentQuery.updateAppt(contact, title, desc, loc, type, start, end, cId, uId, aId);
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setTitle("Confirmed");
                        confirm.setContentText("The appointment has been updated.");
                        confirm.showAndWait();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            apptTable.setItems(AppointmentQuery.getAppointments());
            allTgl.fire();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        }

    }

    /**Goes back to the first screen.
     * @param actionEvent clicking the home button triggers this method.
     */
    public void onMainScreenBtn(ActionEvent actionEvent) throws IOException {
        goBackToMain(actionEvent);

    }
    /**Goes back to the first screen. A helper function to call when this is needed.
     * @param actionEvent clicking the home button triggers this method.
     */
    private void goBackToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**A method to fill in the Time ComboBox. Allow scheduling in increments of 30 minutes.
     *
     */
    private void populateTime() {
        ObservableList<String> time = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);

        time.add(start.toString());
        while (start.isBefore(end)) {
            start = start.plusMinutes(30);
            time.add(start.toString());
        }

        startTime.setItems(time);
        endTime.setItems(time);
    }

    /**Populates the contacts ComboBox. Pulls the data from a database.
     *
     */
    private void populateContacts() throws SQLException {
        ObservableList<String> contactsComboList = FXCollections.observableArrayList();

        try {
            ObservableList<Contact> contacts = ContactQuery.getContacts();
            assert contacts != null;
            for (Contact contact: contacts) {
                if (!contactsComboList.contains(contact.getName()))
                contactsComboList.add(contact.getName());
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        contactCombo.setItems(contactsComboList);
    }

    /**Populates the Customer ID ComboBox. Pulls data from the database.
     *
     */
    private void populateCustId() {
        ObservableList<Integer> custIds = FXCollections.observableArrayList();
        try {
            ObservableList<Customer> custys = CustomerQuery.getCustomers();
            assert custys != null;
            for (Customer custy: custys) {
                custIds.add(custy.getId());
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        custIdCombo.setItems(custIds);

    }
    /**Populates the Users ID ComboBox. Pulls data from the database.
     *
     */
    private void populateUsers() {
        ObservableList<Integer> usersIdList = FXCollections.observableArrayList();
        try {
            ObservableList<User> users = UserQuery.getUsers();
            assert users != null;
            for (User user: users) {
                usersIdList.add(user.getId());
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        userId.setItems(usersIdList);
    }
    /**Populates the Type ID ComboBox. I created a set list categories to choose from..
     *
     */
    private void populateType() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll("Surgery", "Physical", "Covid Test", "Checkup", "Follow-up");
        typeComboBox.setItems(types);
    }
    /**Fills the text fields and other inputs with data from the selected contact. If no contact is selected, then nothing happens
     *
     */
    public void onTableClicked(MouseEvent mouseEvent) {
        if (apptTable.getSelectionModel().getSelectedItem() != null) {
            Appointment selectedAppt = apptTable.getSelectionModel().getSelectedItem();
            apptId.setText(String.valueOf(selectedAppt.getId()));
            apptTitle.setText(selectedAppt.getTitle());
            apptDesc.setText(selectedAppt.getDesc());
            apptLocation.setText(selectedAppt.getLocation());
            contactCombo.setValue(selectedAppt.getContact());
            typeComboBox.setValue(selectedAppt.getType());
            startDateTime.setValue(selectedAppt.getStartDate());
            startTime.setValue(String.valueOf(selectedAppt.getStartTime().toLocalTime()));
            endDateTime.setValue(selectedAppt.getEndDate());
            endTime.setValue(String.valueOf(selectedAppt.getEndTime().toLocalTime()));
            custIdCombo.setValue(selectedAppt.getCustId());
            userId.setValue(selectedAppt.getUserId());
        }
    }



    /**Initializes the view. Loads the comboboxes with data, and has the table ready to display data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        populateType();
        populateTime();
        populateCustId();
        populateUsers();
        try {
            populateContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            appts = AppointmentQuery.getAppointments();
            apptTable.setItems(appts);
            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onSelectCustId(ActionEvent actionEvent) throws SQLException {
        Integer cid = custIdCombo.getValue();
        customerTextField.setText(Objects.requireNonNull(CustomerQuery.getCustById(cid)).getName());
    }

    /**Method to change the view of the table. Changes to monthly, weekly, or all appointments
     * @param actionEvent clicking a toggle triggers this function.
     */
    public void onToggleClick(ActionEvent actionEvent) {
        if (allTgl.isSelected()) {
            try {
                appts = AppointmentQuery.getAppointments();
                apptTable.setItems(appts);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (monthlyTgl.isSelected()) {
            try {
                appts = AppointmentQuery.getApptsLastMonth();
                apptTable.setItems(appts);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (weeklyTgl.isSelected()) {
            try {
                appts = AppointmentQuery.getApptsLastWeek();
                apptTable.setItems(appts);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
