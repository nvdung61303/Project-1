package mainscreen;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import javafx.util.Pair;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import game.*;
import javafx.scene.input.MouseEvent;
public class MainscreenController {

    public Stage solutionStage;
    private Scene scene;
    private Parent root;
    private Stopwatch stopwatch;
    ToggleGroup group = new ToggleGroup();
    List<InitialCell> initialCells = new ArrayList<>();
    /*------------------------
     * fx:id from SceneBuilder
     ------------------------*/

    // buttons
    @FXML
    private Button checkButton;
    @FXML
    private Button giveUpButton;
    @FXML
    private Button startButton;
    @FXML
    private Button hintButton;

    // timer
    @FXML
    private Label timer;

    // Sudoku board
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

    // The array to load into the board
    SudokuBoard sudokuBoard = new SudokuBoard();
    int[][] initialBoard= new int[9][9];


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
            openSolutionScene(event, false);
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
        hintButton.setDisable(false);
        hintButton.setVisible(true);
        // the stopwatchsttart
        startStopwatch();
        // initialize the Sudoku board
        initializeSudokuBoard();
    }
    @FXML
    void checkButtonPressed(ActionEvent event) {
        // check if the board is filled
        if (!containsZero(sudokuBoard.mt)) {
            if (sudokuBoard.isValid()) {
                // the board is filled and correct
                System.out.println("Correct");
                // stop the stopwatch
                String finalTime = stopStopwatch();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Congratulations!");
                alert.setHeaderText("You have completed the Sudoku game in: " + finalTime +  "\nDo you want to see the answers?");
                alert.setContentText("Choose your option:");
                // add "Yes" and "No" buttons to the dialog
                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeNo = new ButtonType("No");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeYes) {
                    // user clicked "Yes" -> Stop the stopwatch + open new scene to display answers
                    openSolutionScene(event, true);
                } else {
                    // user clicked "No" -> continue the Sudoku game
                    System.out.println("restartting...");
                    openNewGameScene(event);
                }
            } else {
                // the board is filled but incorrect
                System.out.println("Incorrect");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect!");
                alert.setHeaderText("The board is incorrect!");
                alert.setContentText("Please check carefully and try again!");
                alert.showAndWait();
    
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

    @FXML
    void hintButtonPressed(MouseEvent event) {
        // When the button is pressed, change the color of the invalid cells to red
        group.selectToggle(null);
        List<Pair<Integer, Integer>> invalidCells = checkBoard(sudokuBoard);
        System.out.println(invalidCells);
        for (Pair<Integer, Integer> pair : invalidCells) {
            int row = pair.getKey();
            int col = pair.getValue();
            for (Toggle toggle : group.getToggles()) {
                InputCell button = (InputCell) toggle;
                if (button.getRow() == row && button.getCol() == col) {
                    button.isWrong();
                    break;
                }
            }
            for (InitialCell cell : initialCells) {
                if (cell.getRow() == row && cell.getCol() == col) {
                    cell.isWrong();
                    break;
                }
            }
        }

    }

    @FXML
    void hintButtonReleased(MouseEvent event) {
        for (Toggle toggle : group.getToggles()) {
            InputCell button = (InputCell) toggle;
            if (button.isSelected()) {
                // The cell is selected, set its background color to light gray
                button.selected();;
            } else {
                // The cell is not selected, set its background color to transparent
                button.unselected();
            }
        }
        for (InitialCell cell : initialCells) {
            cell.defaultStyle();
        }
    };
    /*------------------------
     * Scene Change Functions
     ------------------------*/

    public void openSolutionScene(ActionEvent event, boolean result) {
        try {
            // Load the FXML file of the new scene;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SolutionScreen.fxml"));
            solutionStage =  (Stage)((Node)event.getSource()).getScene().getWindow();
            root = loader.load();
            scene = new Scene(root, 1080, 720);
            // Create a new stage
            SolutionScreenController controller = loader.getController();
            controller.setSolved(result);
            controller.setSolutions(sudokuBoard.sdkSolutions);
            controller.setInitialBoard(initialBoard);
            controller.setCnt(sudokuBoard.printsolutions());
            controller.updateMessages();
            controller.setStage(solutionStage);
            // Set the new scene
            // Calculate the center coordinates of the screen
            solutionStage.setTitle("OOPS!");
            solutionStage.setScene(scene);
            solutionStage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            solutionStage.setX((primScreenBounds.getWidth() - solutionStage.getWidth()) / 2);
            solutionStage.setY((primScreenBounds.getHeight() - solutionStage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }    
            // Handle the exception (e.g., show an error message)
        System.out.println("Opening Answers Scene...");
        }
    /*------- 
    Utilities 
    ---------*/

    // 1. Stopwatch

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

    // 2. Sudoku board

    // initialize the Sudoku board
    void initializeSudokuBoard() {
        sudokuBoard.fill();;
        sudokuBoard.generate();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard.mt[i][j] != 0){
                    initialBoard[i][j] = 1;
                } else {
                    initialBoard[i][j] = 0;
                }
            }
        }        


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Get the gridpane corresponding to the row and column
                GridPane gridPane = getGridPane(row, col);
                // Get the value from the array
                int value = sudokuBoard.mt[row][col];  //replace this with the sudoku game generated array
                final int finalRow = row; // Create a final copy of row
                final int finalCol = col; // Create a final copy of col
                // Create a label with the value and add it to the gridpane
                if (value != 0) {
                    InitialCell label = new InitialCell(row, col, String.valueOf(value));
                    label.defaultStyle();
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(56.1);
                    label.setMaxHeight(56.1);
                    label.setTranslateX(1);
                    initialCells.add(label);
                    gridPane.add(label, col % 3, row % 3);
                }
                else if(value==0){
                    InputCell cell = new InputCell(row, col);
                    cell.unselected();
                    cell.setAlignment(Pos.CENTER);
                    cell.setPrefWidth(56.03);
                    cell.setPrefHeight(56.03);
                    cell.setTranslateX(1);
                    cell.setPadding(Insets.EMPTY);
                    cell.setToggleGroup(group); // add the button to the group
                    // set the button's on click event
                    cell.setOnAction(event -> {
                        if (group.getSelectedToggle() == cell) {
                            event.consume(); // prevent the button from being deselected
                            return;
                        }
                    });
                    cell.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                        if (cell.isSelected()) { // prevent the button from being change color when it is selected again
                            mouseEvent.consume();
                        }
                    });
                    cell.setOnKeyPressed(keyEvent -> {
                        String input = keyEvent.getText();
                        // check if the input is a digit from 1 to 9
                        if (input.matches("[1-9]")) {
                            cell.setText(input);
                            int inputValue = Integer.parseInt(input);
                            sudokuBoard.updateValue(finalRow, finalCol, inputValue);
                            printBoard(sudokuBoard.mt);
                        } else {
                            keyEvent.consume();
                        }
                    });
                    gridPane.add(cell, col % 3, row % 3);
                }
            }
        }
         // set the button's on deselection event
         group.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            // a button is deselected 
            if (oldToggle != null && newToggle == null) {
                ((InputCell) oldToggle).unselected();
            }
            // a button is selected for the first time or a different button is selected
            else if (oldToggle != newToggle && newToggle != null) {
                if (oldToggle != null) {
                    ((InputCell) oldToggle).unselected();
                }
                ((InputCell) newToggle).selected();
            }
            // the same button is clicked
            else if (oldToggle != null && newToggle != null && oldToggle == newToggle) {
                ((InputCell) newToggle).selected();
            }
        });
    }
    // determine the gridpane based on the row and column indices
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
    //print the board in console
    void printBoard(int[][] board){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    //check if the board contains 0
    public boolean containsZero(int[][] array) { 
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // find the invalid cells
    public List<Pair<Integer, Integer>> checkBoard(SudokuBoard sudokuBoard) {
        List<Pair<Integer, Integer>> invalidCells = new ArrayList<>();
        
        for (int row = 0; row < 9; row++) {
            outer:
            for (int col = 0; col < 9; col++) {
                int cellValue = sudokuBoard.mt[row][col];
                if (cellValue == 0) continue;  // skip empty cells
                // check the other cells in the same row, column, and subgrid
                for (int otherRow = 0; otherRow < 9; otherRow++) {
                    for (int otherCol = 0; otherCol < 9; otherCol++) {
                        if (row == otherRow && col == otherCol) continue;  // skip the cell itself
                        int otherValue = sudokuBoard.mt[otherRow][otherCol];
                        int gridRow = row / 3;
                        int gridCol = col / 3;
                        int otherGridRow = otherRow / 3;
                        int otherGridCol = otherCol / 3;
                        if (cellValue == otherValue && (row == otherRow || col == otherCol || (gridRow == otherGridRow && gridCol == otherGridCol))) {
                            // the value is the same and the cell is in the same row, column, or subgrid
                            invalidCells.add(new Pair<>(row, col));
                            invalidCells.add(new Pair<>(otherRow, otherCol));
                            continue outer;  // continue with the next cell
                        }
                    }
                }
            }
        }
        return invalidCells;
    }
    @FXML
    void initialize() {
        Font.loadFont(getClass().getResource("/resources/fonts/Glacial/GlacialIndifference-Regular.otf").toExternalForm(), 28);
    }
}
