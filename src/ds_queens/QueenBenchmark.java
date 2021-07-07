/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds_queens;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import utility.Timekeeper;

/**
 *
 * @author Jonas
 */
public class QueenBenchmark {
    
    private final String[] BENCHMARK_NAMES = {"BS", "MT"};
//    private final int[] COUNTS = {10000, 20000, 40000, 80000};
    private int[] COUNTS = {1, 2, 4, 8};
//    private final int[] COUNTS = {1, 1};
    private final Queue<String> chainsSizes = new LinkedList<>();
    private final Timekeeper timekeeper;
    int size = 0;
    BasicSearch BS = null;//= new BasicSearch(size);
        //b.setText(Integer.toString(BS.findAllPlacements(0) / (size-1)));
    int threadCount = 4;
    counter StaticCounter = new counter();
    int solutionCount = 0;
    CT_Latch countdown = new CT_Latch(4);
        
    public QueenBenchmark(int n, int t, boolean multi){
        size = n;
        if(!multi)
            COUNTS = new int []{1};
        timekeeper = new Timekeeper(COUNTS);
        BS = new BasicSearch(size);
        threadCount = t;
        
    }
    
//    public void startBenchmark(){
//        
//    }
    
        void startBenchmark() throws InterruptedException, FileNotFoundException, IOException {
try {
    
            System.out.println("Board size: " + size + " | Threads: " + threadCount);
            chainsSizes.add("   kiekis      " + BENCHMARK_NAMES[0] + "   " + BENCHMARK_NAMES[1]);
            for (int k : COUNTS) {
                
                timekeeper.startAfterPause();
                timekeeper.start();

                for (int i = 0; i < k; i++) {
                    BS.findAllPlacements(0);
                }
                timekeeper.finish(BENCHMARK_NAMES[0]);
                
                for (int i1 = 0; i1 < k; i1++) {
                    StaticCounter.count = 0;
                    countdown = new CT_Latch(threadCount);
                    SearchMultiThread MS = null;
        
                    for(int i = 0; i < threadCount; i++) {
                        if(i == threadCount-1){
                            MS = new SearchMultiThread(size / threadCount * i, size, size, "Thread_" + Integer.toString(i));
                            MS.start();
                        }
                        else{
                            MS = new SearchMultiThread(size / threadCount * i, size / threadCount * (i+1), size, "Thread_" + Integer.toString(i));
                            MS.start();
                        } 
                    }

                    countdown.latch.await(); // Wait for countdown
            //      System.out.println("Total Sum: " + Integer.toString(StaticCounter.count)); //testing
                }
                timekeeper.finish(BENCHMARK_NAMES[1]);


                
                
//                timekeeper.finish(BENCHMARK_NAMES[2]);
                
//                timekeeper.finish(BENCHMARK_NAMES[3]);


                timekeeper.seriesFinish();
            }

            StringBuilder sb = new StringBuilder();
            chainsSizes.forEach(p -> sb.append(p).append(System.lineSeparator()));
            //timekeeper.logResult(sb.toString());
            timekeeper.logResult("Finished");
            System.out.println();
        } catch (Exception e) {
            timekeeper.logResult(e.getMessage());
        }
    }
}
