package mainscreen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("Mainscreen.fxml"));

        // Set up the primary stage
        primaryStage.setTitle("SUDOKU");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);

        // Show the stage
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        primaryStage.setOnCloseRequest(event -> {
        // Perform any cleanup or necessary actions
        Platform.exit();
});
    }

    public static void main(String[] args) {
        launch(args);
    }
}
