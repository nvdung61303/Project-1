package mainscreen;

import java.util.ArrayList;
import java.util.List;

import game.InitialCell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SolutionScreenController {
    @FXML
    private Button confirmButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button prevButton;
    @FXML
    private Button showAnswerButton;

    // subgrids
    @FXML
    private GridPane bigGridPane;
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

    // message
    @FXML
    private Label message1;
    @FXML
    private Label message2;

    private Stage stage;
    public boolean isSolved;
    public List<int[][]> solutions = new ArrayList<>();
    public int arrayIndex = 0;
    public int[][] initialBoard = new int[9][9];
    public int cnt;


    @FXML
    void confirmButtonPressed(ActionEvent event1) {
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
        stage.setOnCloseRequest(event -> {
        // Perform any cleanup or necessary actions
            Platform.exit();
            System.exit(0);
        });

    } catch (Exception e) {
        System.out.println(e);
        
        }    
    }

    @FXML
    void nextButtonPressed(ActionEvent event) {
        if (arrayIndex < solutions.size() - 1) {
            arrayIndex++;
            clearSudokuBoard();
            initializeSudokuBoard(arrayIndex);
        }

    }

    @FXML
    void prevButtonPressed(ActionEvent event) {
        if (arrayIndex > 0) {
            arrayIndex--;
            clearSudokuBoard();
            initializeSudokuBoard(arrayIndex);
        }
    }

    
    @FXML
    void showAnswerButtonPressed(ActionEvent event) {
        prevButton.setDisable(false);
        prevButton.setVisible(true);
        nextButton.setDisable(false);
        nextButton.setVisible(true);
        confirmButton.setDisable(false);
        confirmButton.setVisible(true);
        showAnswerButton.setDisable(true);
        showAnswerButton.setVisible(false);
        bigGridPane.setDisable(false);

        initializeSudokuBoard(arrayIndex);
        
    }


    void clearSudokuBoard() {
    // Loop through each row and column of the array
    for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
            // Get the gridpane corresponding to the row and column
            GridPane gridPane = getGridPane(row, col);
            // Create a new list to store labels
            List<Node> labels = new ArrayList<>();
            // Iterate over the children of the gridPane
            for (Node node : gridPane.getChildren()) {
                // Check if the node is a label
                if (node instanceof Label) {
                    labels.add(node);
                }
            }
            // Remove all labels from the gridPane
            gridPane.getChildren().removeAll(labels);
        }
    }
}

    void initializeSudokuBoard(int index) {
        int[][] solution = solutions.get(index);
        // Loop through each row and column of the array
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Get the gridpane corresponding to the row and column
                GridPane gridPane = getGridPane(row, col);
                // Get the value from the array
                int value = solution[row][col];  //replace this with the sudoku game generated array
                // Create a label with the value and add it to the gridpane
                if (initialBoard[row][col] != 0) {
                    InitialCell label = new InitialCell(row, col, String.valueOf(value));
                    label.defaultStyle();
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(56.1);
                    label.setMaxHeight(56.1);
                    label.setTranslateX(1);
                    gridPane.add(label, col % 3, row % 3);
                } else {
                    InitialCell label = new InitialCell(row, col, String.valueOf(value));
                    label.defaultStyle2();
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(56.1);
                    label.setMaxHeight(56.1);
                    label.setTranslateX(1);
                    gridPane.add(label, col % 3, row % 3);
                }
                }
            }
    
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

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSolutions(List<int[][]> solutions) {
        this.solutions = solutions;
    }

    public void setSolved(boolean result) {
        this.isSolved = result;
    }
    public void setMessage1(boolean isSolved) {
        if (isSolved) {
            this.message1.setText("Well Played! Here is the solution");
        } else {
            this.message1.setText("Tough luck! Here is the solution");
        }
    }
    public void setMessage2(int cnt) {
        this.message2.setText("This puzzle has: " + cnt + " solutions");
    }

    public void setInitialBoard(int[][] initialBoard) {
        this.initialBoard = initialBoard;
    }

    public void updateMessages() {
        setMessage1(isSolved);
        setMessage2(cnt);
    }
     

    @FXML
    void initialize() {
        assert confirmButton != null : "fx:id=\"comfirmButton\" was not injected: check your FXML file 'SolutionScreen.fxml'.";
        assert nextButton != null : "fx:id=\"nextButton\" was not injected: check your FXML file 'SolutionScreen.fxml'.";
        assert prevButton != null : "fx:id=\"prevButton\" was not injected: check your FXML file 'SolutionScreen.fxml'.";
        // initializeSudokuBoard(arrayIndex);
    }
}

