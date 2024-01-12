package mainscreen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SolutionScreenController {
    @FXML
    private Button confirmButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    private Stage stage;

    @FXML
    void confirmButtonPressed(ActionEvent event) {
        try {
        Stage currentStage = (Stage) confirmButton.getScene().getWindow();
        currentStage.close();
                // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("Mainscreen.fxml"));

        // Set up the stage
        stage = new Stage();
        stage.setTitle("SUDOKU");
        stage.setScene(new Scene(root, 1280, 720));
        stage.setResizable(false);
        // Show the stage
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    } catch (Exception e) {
        System.out.println(e);
        
        }    
    }


    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }


    @FXML
    void nextButtonPressed(ActionEvent event) {

    }

    @FXML
    void prevButtonPressed(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert confirmButton != null : "fx:id=\"comfirmButton\" was not injected: check your FXML file 'SolutionScreen.fxml'.";
        assert nextButton != null : "fx:id=\"nextButton\" was not injected: check your FXML file 'SolutionScreen.fxml'.";
        assert prevButton != null : "fx:id=\"prevButton\" was not injected: check your FXML file 'SolutionScreen.fxml'.";

    }
}

