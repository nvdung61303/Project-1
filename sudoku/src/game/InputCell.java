package game;

import javafx.scene.control.ToggleButton;

public class InputCell extends ToggleButton{
    public int row;
    public int col;

    public InputCell(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setCol(int col){
        this.col = col;
    }
    public void selected(){
        this.setStyle("font-family: 'Glacial';" +
        "-fx-font-size: 28px;" +
        "-fx-text-fill: #4464b6;" +
        "-fx-background-color: silver;" +
        "-fx-background-radius: 0;" +
        "-fx-content-display: CENTER;" +
        "-fx-border-width: 1px;");
    }
    

    public void unselected(){
        this.setStyle("font-family: 'Glacial';" +
        "-fx-font-size: 28px;" +
        "-fx-text-fill: #4464b6;" +
        "-fx-background-color: transparent;" +
        "-fx-background-radius: 0;" +
        "-fx-content-display: CENTER;" +
        "-fx-border-width: 1px;");;
    }

    public void isWrong(){
        this.setStyle("font-family: 'Glacial';" +
        "-fx-font-size: 28px;" +
        "-fx-text-fill: #e26272;" +
        "-fx-background-color: #ebcbd4;" +
        "-fx-background-radius: 0;");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InputCell cell = (InputCell) obj;
        return row == cell.row && col == cell.col;
    }
}  
