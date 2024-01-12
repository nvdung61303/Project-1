package game;

import java.util.*;
public class SudokuBoard {
    public int[][] mt = new int[9][9];

    public void fill(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++)
                mt[i][j] = 0;
        }
        //this.mt = mt;
    }
     boolean check(int n,int r, int c,int[][] board){
        for(int i=0;i<9;i++){
            if((n == board[r][i])||(n == board[i][c])||(n == board[3*(r/3)+i/3][3*(c/3)+i%3])) return false;
        }
        return true;
    }
    public void generate(){
        int[][] board = new int[9][9];
        ArrayList<Integer> list = new ArrayList<Integer>(9);
        for(int i=1;i<=9;i++) list.add(i);


        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++)
                board[i][j] = 0;

        }

        //fill(board);
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                Collections.shuffle(list);

                for(int x: list){

                    if(check(x,r,c,board)) board[r][c] = x;

                }
            }
        }
        Random rand = new Random();
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(rand.nextInt(2)==1) board[i][j] = 0;
            }
        }
             
        this.mt = board;
        printboard();  
    }
    void printboard(){

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(mt[i][j]+" ");
            }
            System.out.println();


        }
    }

}
