package game;

import javafx.scene.control.Label;

public class InitialCell extends Label {
    public int row;
    public int col;


    public InitialCell(int row, int col, String value){
        super(value);
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
    public void defaultStyle(){
        this.setStyle("font-family: 'Glacial';" +
        "-fx-font-size: 28px;" +
        "-fx-text-fill: black;" +
        "-fx-background-color: #DCDCDC;" +
        "-fx-background-radius: 0;" +
        "-fx-content-display: CENTER;" +
        "-fx-border-width: 1px;");
    }

    public void defaultStyle2(){
        this.setStyle("font-family: 'Glacial';" +
        "-fx-font-size: 28px;" +
        "-fx-text-fill: black;" +
        "-fx-background-color: transparent;" +
        "-fx-background-radius: 0;" +
        "-fx-content-display: CENTER;" +
        "-fx-border-width: 1px;");
    }

    public void isWrong(){
        this.setStyle("font-family: 'Glacial';" +
        "-fx-font-size: 28px;" +
        "-fx-text-fill: black;" +
        "-fx-background-color: #edcbd4;" +
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
        InitialCell cell = (InitialCell) obj;
        return row == cell.row && col == cell.col;
    }
}  

