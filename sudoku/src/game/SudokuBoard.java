package game;

import java.util.*;
public class SudokuBoard {
    public int[][] mt = new int[9][9];
    int[][] temp = new int[9][9];
    public List<int[][]> sdkSolutions = new ArrayList<>();
    public int cnt =0;

    public void fill(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++)
                mt[i][j] = 0;
        }
        //this.mt = mt;
    }


    boolean check(int n,int r, int c,int[][] board){
        for(int i=0;i<9;i++){
            if((i != c && n == board[r][i])
            ||(i != r && n == board[i][c])
            ||((3*(r/3)+i/3 != r || (3*(c/3)+i%3) != c) && n == board[3*(r/3)+i/3][3*(c/3)+i%3])) 
            return false;
        }
        return true;
    }

    public boolean isValid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int num = mt[row][col];
                if (num != 0 && !check(num, row, col, mt)) {
                    return false;
                }
            }
        }
        // All cells are valid
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
        Random rand = new Random();
        //Neu so loi giai khac 0 thi board hop le
        while(cnt==0){
            for(int r=0;r<9;r++){
                for(int c=0;c<9;c++){
                    Collections.shuffle(list);
                    //if(rand.nextInt(2)==1){
                        for(int x: list){
                        {
                            if(check(x,r,c,board)) board[r][c] = x;
                        }
                        
                        }
                    //}
                    
                }
            }
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(rand.nextInt(2)==1) board[i][j] = 0;
                }
            }
                 
            this.mt = board;
            this.temp = board;
            solve(0,0);

        }
        
        
        //printboard();  
        printsolutions();

    }

    void solve(int r,int c){
        // if(this.cnt>64) {
        //     //System.out.println("Qua Nhieu Loi Giai");
        //     return;
        // } 
        if (r == 9) {

            int[][] solution = new int[9][9];
            for (int i = 0; i < 9; i++) {
                System.arraycopy(temp[i], 0, solution[i], 0, 9);
            }
            if (cnt < 30) {
                this.sdkSolutions.add(solution);
            }
            this.cnt++;
            return;
        }
        
        int nextRow = (c == 8) ? (r + 1) : r;
        int nextCol = (c + 1) % 9;

        if (this.mt[r][c] != 0) {
            solve(nextRow, nextCol);
            return;
        }

        for (int num = 1; num <= 9; ++num) {
            if (check(num, r, c, this.temp)) {
                this.temp[r][c] = num;
                solve(nextRow, nextCol);
                this.temp[r][c] = 0;  
            }
        }
    }
    
    public int printsolutions(){
        // int i=0;
        // System.out.println();
        // for (int[][] solution : sdkSolutions) {
        //     System.out.println("loi giai thu " + ++i);
        //     System.out.println();
        //     for (int[] row : solution) {
        //         for (int value : row) {
        //             System.out.print(value + " ");
        //         }
        //         System.out.println();
        //     }
        //     System.out.println();
        // }
        System.out.println(this.cnt);
        return cnt;
        //if(sdkSolutions.size()>=65) System.out.println("Co qua nhieu loi giai" +" " + sdkSolutions.size());
    }

    void printboard(){

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(mt[i][j]+" ");
            }
            System.out.println();


        }
    }

    public void updateValue(int row, int col, int value){
        this.mt[row][col] = value;
    }

}
