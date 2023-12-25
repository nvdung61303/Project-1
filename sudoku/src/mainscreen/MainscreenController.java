package mainscreen;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import game.*;


public class MainscreenController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stopwatch stopwatch;

    /*------------------------
     * fx:id from SceneBuilder
     ------------------------*/
    @FXML
    private Button checkButton;
    @FXML
    private Button giveUpButton;
    @FXML
    private Button startButton;
    @FXML
    private Label timer;
    @FXML
    private GridPane grid00;
    @FXML
    private GridPane grid01;
    @FXML
    private GridPane grid02;
    @FXML
    private GridPane grid10;
    @FXML
    private GridPane grid11;
    @FXML
    private GridPane grid12;
    @FXML
    private GridPane grid20;
    @FXML
    private GridPane grid21;
    @FXML
    private GridPane grid22;


    // The sample array to test the input method
    int[][] sample = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };


    @FXML
    private URL location;

    /* --------------
     Button Listener
     ----------------*/
    @FXML
    void giveUpButtonPressed(ActionEvent event) {
        // Make a dialog to ask user wether give up or not
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Give Up?");
        alert.setHeaderText("Are you sure you want to give up?");
        alert.setContentText("Choose your option:");

        // Add "Yes" and "No" buttons to the dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show the dialog and wait for the response
        Optional<ButtonType> result = alert.showAndWait();

        // Handle the user's choice
        if (result.isPresent() && result.get() == buttonTypeYes) {
            // User clicked "Yes" - Stop the stopwatch + Open new scene to display answers
            stopStopwatch();
            openSolutionScene(event);
        } else {
            // User clicked "No" - Continue the Sudoku game
            System.out.println("Continuing playing...");
        }
    }

    @FXML
    void startButtonPressed(ActionEvent event) {
        
        // Hide Start button, Show Give Up and Check Button
        startButton.setDisable(true);
        startButton.setVisible(false);

        giveUpButton.setDisable(false);
        giveUpButton.setVisible(true);

        checkButton.setDisable(false);
        checkButton.setVisible(true);
        
        // The stopwatch ttart
        startStopwatch();

        // Initialize the Sudoku board
        initializeSudokuBoard();
        
    }

    @FXML
    void checkButtonPressed(ActionEvent event) {
        stopStopwatch();
    }

    private void openSolutionScene(ActionEvent event) {
        try {
            // Load the FXML file of the new scene;
            root = FXMLLoader.load(getClass().getResource("SolutionScreen.fxml"));
            
            // Create a new stage
            stage =  ((Stage)((Node)event.getSource()).getScene().getWindow());
            // Set the new scene
            // Calculate the center coordinates of the screen
            scene = new Scene(root, 720, 720);
            stage.setTitle("OOPS!");

            stage.setScene(scene);

            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            System.out.println(e);
            
        }    
            // Handle the exception (e.g., show an error message)
        System.out.println("Opening Answers Scene...");
        }

    /*------- 
    Utilities 
    ---------*/

    // Create a thread and start the stopwatch
    public void startStopwatch() {
        stopwatch = new Stopwatch();

        new Thread(() -> {
            stopwatch.start();
            while (stopwatch.isRunning()) {
                String formattedTime = stopwatch.displayTime();
                
                // Update UI component on the JavaFX application thread
                Platform.runLater(() -> timer.setText("TIMER: " + formattedTime));
            }
        }).start();
    }

    // Stop the stopwatch if 
    public void stopStopwatch() {
        if (stopwatch != null) {
            stopwatch.stop();
        }
    }

    private void initializeSudokuBoard() {
        // Loop through each row and column of the array
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Get the gridpane corresponding to the row and column
                GridPane gridPane = getGridPane(row, col);
    
                // Get the value from the array
                int value = sample[row][col];  //replace this with the sudoku game generated array
    
                // Create a label with the value and add it to the gridpane
                if (value != 0) {
                    Label label = new Label(String.valueOf(value));
                    label.getStyleClass().add("grid-label");
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setMaxHeight(Double.MAX_VALUE);
                    gridPane.add(label, col % 3, row % 3);
                }
            }
        }
    }

    private GridPane getGridPane(int row, int col) {
        // Determine the gridpane based on the row and column indices
        if (row < 3) {
            if (col < 3) {
                return grid00;
            } else if (col < 6) {
                return grid01;
            } else {
                return grid02;
            }
        } else if (row < 6) {
            if (col < 3) {
                return grid10;
            } else if (col < 6) {
                return grid11;
            } else {
                return grid12;
            }
        } else {
            if (col < 3) {
                return grid20;
            } else if (col < 6) {
                return grid21;
            } else {
                return grid22;
            }
        }
    } 
    

    @FXML
    void initialize() {

    }
}
