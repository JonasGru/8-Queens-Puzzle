/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds_queens;

import extendsFX.BaseGraphics;
import static java.awt.Font.SANS_SERIF;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 *
 * @author Jonas Gruzdys IFA-8-2
 */
public class DS_Queens extends BaseGraphics{

    int n = 8; //laikinas
    boolean[][] board = new boolean[n][n];
    String c = "\u265B";
    //String queenMarker = c;
    String queenMarker = "Q";
    private final static Random RANDOM = new Random(6969);  // Atsitiktinių sk. generatorius
    int queensPlaced = 0;
    int collumT = 0;
    int rowT = 0;
    int start = RANDOM.nextInt(n);
    int lastStart = start;
    private VisualParameters vip = null;
    int Min_n = 0;
    int Max_n = 15;
    counter StaticCounter = new counter();
    
    int solutionCount = 0; 
    CT_Latch countdown = new CT_Latch(4);
    int threadCount = 4;
    
    public Text b = addText("");
    //Text b = addText("");
    
    void startGame(int size){
        n = size;
        if (n >= Min_n && n <= 30){
            b.setText("");
            gc.setFont(new Font(SANS_SERIF, canvasW/n -2));
            board = new boolean[n][n];
            clearCanvas();

            queensPlaced = 0;

            for (int i = 0; i < n; i++) { //šachmatų lenta
                for (int j = 0; j < n; j++) {
                gc.setFill((i + j) % 2 == 0? Color.WHITE : Color.BLACK);
                gc.fillRect(canvasW/n * i, canvasW/n * j, canvasW/n, canvasH/n);
                }
            }

//            start = RANDOM.nextInt(n); //kad paspaudus mygtuką pasirodytų kitas atsakymas
//            if(start == lastStart)
//                start = (start += 2) % n;
//            lastStart = start;

            start = 0;
            solutionCount = 0;
            
            boolean answer = false;
            while (!answer && 
                    start != n){ //atsakmo ieškojimas/ rekursijos pradžia
                answer = QueenSearch((start++) % n, 0, board);
            }
            //QueenSearch(0, 0, board, 0, 0); // vienodas variantas visada
            drawQueens(board);//nupiešia karalienes
            //b.setText(Integer.toString(solutionCount / (n-1)));
        }
        else {
            errorMessege();
        }
    }
    
    void errorMessege(){
        //b = addText("N cannot be < 4");
        b.setText("n cannot be < " + Integer.toString(Min_n) + "  or > " + Integer.toString(Max_n) );
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
                    return true;
                }
            }
            //jei nebuvo rastas galimas atsakymas
            row--;
            b[collum][row] = false;
            queensPlaced--;
        } 
        return false;
    }
    
    void drawQueens(boolean[][] answer){
        gc.setFill(Color.RED.darker());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            if(answer[i][j])
                gc.fillText(queenMarker, canvasW/n * i + canvasH/(n*10), canvasH/n *j + canvasH/(n*1.2));
            }
        }       
        //testing
//        gc.setFill(Color.CYAN.darker());
//        gc.fillText(Integer.toString(queensPlaced), canvasW/n , canvasH/n); //testavimui
    }    
    
    void findAll(int size){
        //startGame(size);
        BasicSearch BS = new BasicSearch(size);
        b.setText(Integer.toString(BS.findAllPlacements(0) / (size-1)));
    }
    
    void searchThreads (int size, int t) throws InterruptedException{
        
        threadCount = t;
        StaticCounter.count = 0;
        countdown = new CT_Latch(threadCount);
        n = size;
        
        SearchMultiThread MS = null;
        
        for(int i = 0; i < threadCount; i++) {
            if(i == threadCount-1){
                MS = new SearchMultiThread(n / threadCount * i, n, size, "Thread_" + Integer.toString(i));
                MS.start();
            }
            else{
                MS = new SearchMultiThread(n / threadCount * i, n / threadCount * (i+1), size, "Thread_" + Integer.toString(i));
                MS.start();
            } 
        }
        
    countdown.latch.await(); // Wait for countdown
    System.out.println("Total Sum: " + Integer.toString(StaticCounter.count));
        
//        MS = new SearchMultiThread(0, 2, 8, "Thread_10");
//        MS.start();
//        SearchMultiThread MS1 = new SearchMultiThread(2, 4, 8, "Thread_20");
//        MS1.start();
//        SearchMultiThread MS2 = new SearchMultiThread(4, 6, 8, "Thread_30");
//        MS2.start();
//        SearchMultiThread MS3 = new SearchMultiThread(6, 8, 8, "Thread_40");
//        MS3.start();
        
//        StaticCounter
//        System.out.println(Integer.toString(MS.sum)); 
//
//    countdown.latch.await(); // Wait for countdown
//    System.out.println("Total Sum: " + Integer.toString(StaticCounter.count));

}
    
    void benchmark(int a, int threads, boolean multi) throws InterruptedException, IOException{
        QueenBenchmark QB = new QueenBenchmark(a, threads, multi);
        QB.startBenchmark();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Karalienės © Jonas Gruzdys");
        setCanvas(Color.CYAN.brighter(), 800, 800);
        super.start(stage);
        gc.save();
        startGame(n);
    }       
    public static void main(String[] args) {
        launch(args);
    }      
    @Override
    public void createControls() {
        addButton("Find new",  e -> startGame(vip.getUserBoardSize()));
        addButton("Find all",  e -> findAll(vip.getUserBoardSize()));
        
        vip = new VisualParameters();
        b = addText("");
        addButton("MT Find all",  e -> {
            try {
                searchThreads(vip.getUserBoardSize(), vip.getThreadCount());
            } catch (InterruptedException ex) {
                Logger.getLogger(DS_Queens.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addButton("Benchmark",  e -> {
            try {
                benchmark(vip.getUserBoardSize(), vip.getThreadCount(), true);
            } catch (InterruptedException ex) {
                Logger.getLogger(DS_Queens.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DS_Queens.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addButton("BenchmarkSingle",  e -> {
            try {
                benchmark(vip.getUserBoardSize(), vip.getThreadCount(), false);
            } catch (InterruptedException ex) {
                Logger.getLogger(DS_Queens.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DS_Queens.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private class VisualParameters{
        final private TextField tfBoardSize = addTextField("n=", "8", 35);
        final private TextField tfThreads = addTextField("t=", "4", 35);
        int getUserBoardSize(){ 
            return Integer.parseInt(tfBoardSize.getText());
        }
        int getThreadCount(){ 
            return Integer.parseInt(tfThreads.getText());
        }
    }    
}
