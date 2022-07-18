/**@author Aaron Anguiano
 * Appointment Scheduling Application.
 * C195
 * JavaDoc Location ./C195-App/javadoc
 */

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;

/**This class creates a Scheduling application.
*/
public class Main extends Application {
    /**This method loads the login screen.
     * @param primaryStage login screen.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**This is the main method. It Launches the Application.
     * @param args String[]
     */
    public static void main(String[] args) {

        launch(args);


    }



}
