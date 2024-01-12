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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import game.*;
import javafx.scene.input.MouseEvent;
public class MainscreenController {

    private Stage solutionStage;
    private Scene scene;
    private Parent root;
    private Stopwatch stopwatch;
    ToggleGroup group = new ToggleGroup();
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
    int[][] board = new int[9][9];
    int[][] initialBoard = new int[9][9];
    int press = 0;
    @FXML
    private URL location;
    /* --------------
     Button Listener
     ----------------*/
    @FXML
    void giveUpButtonPressed(ActionEvent event) {
        // make a dialog to ask user wether give up or not
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Give Up?");
        alert.setHeaderText("Are you sure you want to give up?");
        alert.setContentText("Choose your option:");
        // add "Yes" and "No" buttons to the dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        // Show the dialog and wait for the response
        Optional<ButtonType> result = alert.showAndWait();
        // handle the user's choice
        if (result.isPresent() && result.get() == buttonTypeYes) {
            // user clicked "Yes" -> Stop the stopwatch + Open new scene to display answers
            stopStopwatch();
            openSolutionScene(event);
        } else {
            // user clicked "No" -> Continue the Sudoku game
            System.out.println("Continuing playing...");
        }
    }
    @FXML
    void startButtonPressed(ActionEvent event) {
        // hide Start button, Show Give Up and Check Button
        startButton.setDisable(true);
        startButton.setVisible(false);
        giveUpButton.setDisable(false);
        giveUpButton.setVisible(true);
        checkButton.setDisable(false);
        checkButton.setVisible(true);
        // the stopwatchsttart
        startStopwatch();
        // initialize the Sudoku board
        initializeSudokuBoard();
    }
    @FXML
    void checkButtonPressed(ActionEvent event) {
        // check if the board is filled
        if (!containsZero(board)) {
            // stop the stopwatch
            String finalTime = stopStopwatch();
            // make a dialog to ask user wether give up or not
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText("You have completed the Sudoku game in: " + finalTime +  "\nDo you want to see the answers?");
            alert.setContentText("Choose your option:");
            // add "Yes" and "No" buttons to the dialog
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            // Show the dialog and wait for the response
            Optional<ButtonType> result = alert.showAndWait();
            // handle the user's choice
            if (result.isPresent() && result.get() == buttonTypeYes) {
                // user clicked "Yes" -> Stop the stopwatch + open new scene to display answers
                openSolutionScene(event);
            } else {
                // user clicked "No" -> continue the Sudoku game
                System.out.println("restartting...");
                openNewGameScene(event);
                
            }
        } else {
            // the board is not filled
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("The Sudoku board is not filled yet!");
            alert.setContentText("Please fill in all the empty cells before checking!");
            alert.showAndWait();
        }
    }

    private void openSolutionScene(ActionEvent event) {
        try {
            // Load the FXML file of the new scene;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SolutionScreen.fxml"));
            root = loader.load();
            // Create a new stage
            solutionStage =  ((Stage)((Node)event.getSource()).getScene().getWindow());
            SolutionScreenController controller = loader.getController();
            controller.setStage(solutionStage);
            // Set the new scene
            // Calculate the center coordinates of the screen
            scene = new Scene(root, 720, 720);
            solutionStage.setTitle("OOPS!");
            solutionStage.setScene(scene);
            solutionStage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            solutionStage.setX((primScreenBounds.getWidth() - solutionStage.getWidth()) / 2);
            solutionStage.setY((primScreenBounds.getHeight() - solutionStage.getHeight()) / 2);
        } catch (IOException e) {
            System.out.println(e);
        }    
            // Handle the exception (e.g., show an error message)
        System.out.println("Opening Answers Scene...");
        }
    /*------- 
    Utilities 
    ---------*/
    // create a thread and start the stopwatch
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
    // Stop the stopwatch
    public String stopStopwatch() {
        String finalTime = stopwatch.displayTime();
        if (stopwatch != null) {
            stopwatch.stop();
            finalTime = stopwatch.displayTime();
        }
        return finalTime;
    }


    // initialize the Sudoku board
    private void initializeSudokuBoard() {
        SudokuBoard sudokuBoard = new SudokuBoard();
        sudokuBoard.fill();;
        sudokuBoard.generate();
        board = sudokuBoard.mt;
        initialBoard = sudokuBoard.mt;
        // Loop through each row and column of the array
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Get the gridpane corresponding to the row and column
                GridPane gridPane = getGridPane(row, col);
                // Get the value from the array
                int value = board[row][col];  //replace this with the sudoku game generated array
                final int finalRow = row; // Create a final copy of row
                final int finalCol = col; // Create a final copy of col
                // Create a label with the value and add it to the gridpane
                if (value != 0) {
                    Label label = new Label(String.valueOf(value));
                    label.getStyleClass().add("grid-label");
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setMaxHeight(Double.MAX_VALUE);
                    gridPane.add(label, col % 3, row % 3);
                }
                else if(value==0){
                    ToggleButton button = new ToggleButton();
                    button.setStyle(
                        "font-family: 'Glacial';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight:normal;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 0;");
                    button.setToggleGroup(group); // add the button to the group
                    button.setAlignment(Pos.CENTER);
                    button.setMaxWidth(58);
                    button.setMaxHeight(58);
                    // set the button's on click event
                    button.setOnAction(event -> {
                        if (group.getSelectedToggle() == button) {
                            event.consume(); // prevent the button from being deselected
                            return;
                        }
                    });
                    button.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                        if (button.isSelected()) { // prevent the button from being change color when it is selected again
                            mouseEvent.consume();
                        }
                    });
                    button.setOnKeyPressed(keyEvent -> {
                        String input = keyEvent.getText();
                        // check if the input is a digit from 1 to 9
                        if (input.matches("[1-9]")) {
                            button.setText(input);
                            int inputValue = Integer.parseInt(input);
                            board[finalRow][finalCol] = inputValue;
                            printBoard(board);
                        } else {
                            keyEvent.consume();
                        }
                    });
                    gridPane.add(button, col % 3, row % 3);
                }
            }
        }
         // set the button's on deselection event
         group.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            // a button is deselected 
            if (oldToggle != null && newToggle == null) {
                ((ToggleButton) oldToggle).setStyle(
                        "font-family: 'Glacial';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight:normal;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 0;");
            }
            // a button is selected for the first time or a different button is selected
            else if (oldToggle != newToggle && newToggle != null) {
                if (oldToggle != null) {
                    ((ToggleButton) oldToggle).setStyle(
                        "font-family: 'Glacial';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight:normal;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 0;");
                }
                ((ToggleButton) newToggle).setStyle(
                        "font-family: 'Glacial';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight:normal;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-color: lightgray;" +
                        "-fx-background-radius: 0;");
            }
            // the same button is clicked
            else if (oldToggle != null && newToggle != null && oldToggle == newToggle) {
                ((ToggleButton) newToggle).setStyle(                        
                        "font-family: 'Glacial';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight:normal;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-color: lightgray;" +
                        "-fx-background-radius: 0;");
            }
        });
    }
    // Determine the gridpane based on the row and column indices
    private GridPane getGridPane(int row, int col) {
        if (row < 3) {
            if (col < 3) {
                return grid00;
            } else if (col < 6) {
                return grid10;
            } else {
                return grid20;
            }
        } else if (row < 6) {
            if (col < 3) {
                return grid01;
            } else if (col < 6) {
                return grid11;
            } else {
                return grid21;
            }
        } else {
            if (col < 3) {
                return grid02;
            } else if (col < 6) {
                return grid12;
            } else {
                return grid22;
            }
        }
    } 
    void printBoard(int[][] board){ //print the board in console
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean containsZero(int[][] array) { //check if the board contains 0
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void openNewGameScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml")); // replace with your initial game scene FXML file
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize() {
        Font.loadFont(getClass().getResource("/resources/fonts/Glacial/GlacialIndifference-Regular.otf").toExternalForm(), 28);
    }
}
