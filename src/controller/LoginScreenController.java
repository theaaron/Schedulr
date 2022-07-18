package controller;

import database.AppointmentQuery;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**This is an interface for a LAMBDA method.
 *
 */
interface ShowAlert {
    public void show(String str);
}
/** Controller for logging in and writing attempts to a file.
 * @author Aaron
 */
public class LoginScreenController implements Initializable {
    public Label currentLocation;
    public Label currentTimeZone;
    public Label userLabel;
    public Label passLabel;
    private ResourceBundle resourceBundle;
    public TextField usernameField;
    public TextField passwordField;
    public Button loginButton;
    public Label locationLabel;
    public Label timeZoneLabel;
    public String filename = "login_activity.txt";

    /**This LAMBDA method shows an alert with a given string. This removes the clutter of adding an alert each time its needed.
     * The method takes in a string and places that string into an alert.
     */
    ShowAlert showAlert = (str) -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setContentText(str);
        alert.showAndWait();
    };
    /**Verifies that the username field is not empty. Uses a lambda expression to display an alert.
     * @param user takes a string to check.
     * @return returns a boolean true if the username has text.
     */
    public boolean usernameIsNotEmpty(String user) {
        if (user.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                showAlert.show(resourceBundle.getString("userRequired"));
            }
            return false;
        } else return true;
    }
    /**Verifies that the password field is not empty. Utilizes a LAMBDA expression to display an alert.
     * @param pass takes a string to check.
     * @return returns a boolean true if the password has text.
     */
    public boolean passIsNotEmpty(String pass) {
        if (pass.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                showAlert.show(resourceBundle.getString("passRequired"));
            }
            return false;
        }
        else return true;

    }
    /** Creates a new file for logging if it doesn't already exist.
     *
     */
    private void makeFile() {
        try {
            File newFile = new File(filename);
            if (newFile.createNewFile()) {
                System.out.println("Created " + newFile.getName());
            } else {
                System.out.println("File already exists. Located in " + newFile.getPath());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**Method that shows an alert that relays if there are any upcoming appointments.
     *
     */
    public void upcomingAppts() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime inFifteenMinutes = localDateTime.plusMinutes(15);

        ObservableList<Appointment> upcomingAppts = FXCollections.observableArrayList();

        try {
            ObservableList<Appointment> appts = AppointmentQuery.getAppointments();

            if (appts != null) {
                for (Appointment appt: appts) {
                    if (appt.getStartTime().isAfter(localDateTime) && appt.getStartTime().isBefore(inFifteenMinutes)) {
                        upcomingAppts.add(appt);

                        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle(resourceBundle.getString("apptAlert"));
                            alert.setContentText(
                                    resourceBundle.getString("withinFifteenMinutes") +
                                            "\n" +
                                            resourceBundle.getString("apptId") +
                                            " " +
                                            appt.getId() +
                                            "\n" +
                                            resourceBundle.getString("customer") +
                                            " " +
                                            CustomerQuery.getCustById(appt.getCustId()).getName() +
                                            "\n" +
                                            resourceBundle.getString("date") +
                                            " " +
                                            appt.getStartDate() +
                                            "\n" +
                                            resourceBundle.getString("time") +
                                            " " +
                                            appt.getStartTime().toLocalTime()
                            );
                            alert.setResizable(true);
                            alert.showAndWait();
                        }
                    }
                }
                if (upcomingAppts.size() < 1) {
                    if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(resourceBundle.getString("apptAlert"));
                        alert.setContentText(resourceBundle.getString("noApptsUpcoming"));
                        alert.setResizable(true);
                        alert.showAndWait();
                    }
                }


            } else {
                if (upcomingAppts.size() < 1) {
                    if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(resourceBundle.getString("apptAlert"));
                        alert.setContentText(resourceBundle.getString("noApptsUpcoming"));
                        alert.setResizable(true);
                        alert.showAndWait();
                    }
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**Method that writes details of a successful login to the created file.
     *
     */
    private void successfulLogin() {
        upcomingAppts();

        try {
            FileWriter fileWriter = new FileWriter(filename, true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Login Successful: User: " + usernameField.getText() + " Password: " + passwordField.getText() + " @ " + simpleDateFormat.format(date) + "\n");
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**Method that writes details of a failed login to the created file.
     *
     */
    private void loginFailed() {
        try {
            FileWriter fileWriter = new FileWriter(filename, true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Login Failed: User: " + usernameField.getText() + " Password: " + passwordField.getText() + " @ " + simpleDateFormat.format(date) + "\n");
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**Initializes the view. Sets labels depending on locale.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        resourceBundle = ResourceBundle.getBundle("Language", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")){
            userLabel.setText(resourceBundle.getString("username"));
            passLabel.setText(resourceBundle.getString("password"));
            locationLabel.setText(resourceBundle.getString("location"));
            timeZoneLabel.setText(resourceBundle.getString("timezone"));
            currentLocation.setText(resourceBundle.getString("country"));
            currentTimeZone.setText(String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));
            loginButton.setText(resourceBundle.getString("login"));

        }
        System.out.println("init'd");
    }

    /**Checks the username and password and writes the results to a file. sends you to another screen on a success.
     *
     */
    public void onLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (usernameIsNotEmpty(username) && passIsNotEmpty(password)) {

            makeFile();

            try {
                boolean loggedIn = UserQuery.checkLoginInfo(username, password);
                if (loggedIn) {
                    successfulLogin();

                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();

                        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(resourceBundle.getString("error"));
                            alert.setContentText(resourceBundle.getString("loginScreenError"));
                            alert.showAndWait();
                        }
                    }
                } else {
                    loginFailed();
                    if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(resourceBundle.getString("error"));
                        alert.setContentText(resourceBundle.getString("userPassError"));
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
