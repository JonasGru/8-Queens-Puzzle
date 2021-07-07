/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds_queens;

/**
 *
 * @author Jonas
 */
public class BasicSearch {
    
    int queensPlaced;
    int n;
    boolean[][] board;
    int collumT = 0;
    int rowT = 0;
    int solutionCount = 0;
    
    public BasicSearch(int size) {
        
    n = size;
    prepare();
    
    }
    
    void prepare(){
        
        board = new boolean[n][n];
        queensPlaced = 0;
        collumT = 0;
        rowT = 0;
        solutionCount = 0;
        
    }
    
        public int findAllPlacements(int start){
            boolean answer = false;
//            return 78;
                while (//!answer && 
                    start != n){ //atsakmo ieškojimas/ rekursijos pradžia
                    answer = QueenSearch((start++) % n, 0, board);
                    
            }
                return solutionCount;
        }
    
        boolean QueenSearch(int collum, int row, boolean[][] b){

        if(row >= n && queensPlaced == n){
            board = b;
            //solutionCount++;
            //drawQueens(board);
            
            return true;
            //return false;
        } else if (row >= n)
            return false;

        boolean placable = true;
        
        //loginiai patikrinimai
        collumT = collum;
        rowT = row;
        while(rowT >= 0 && placable){
            if(!b[collum][rowT]){
                rowT--;
            } else placable = false;
        }
        
        rowT = row;
        collumT = collum;
        while(collumT >= 0 && rowT >= 0 && placable){
            if(!b[collumT][rowT]){
                collumT--;
                rowT--;
            } else placable = false;
        }
        
        collumT = collum;
        rowT = row;
        while(collumT <= n-1 && rowT >= 0 && placable){
            if(!b[collumT][rowT]){
                collumT++;
                rowT--;
            } else placable = false;
        }
        
        if(placable){
            b[collum][row] = true;
            queensPlaced++;
            row++;
            //atsakymo paiška ą eilute žemiau
            for (int collumT = collum; (collumT+1) % n != collum; collumT++) {
                boolean foundSolution = QueenSearch((collumT+1) % n, row, b);
                if(foundSolution){
                    
                    solutionCount++;
                    //return true;
                }
            }
            //jei nebuvo rastas galimas atsakymas
            row--;
            b[collum][row] = false;
            queensPlaced--;
        } 
        return false;
    }
}
