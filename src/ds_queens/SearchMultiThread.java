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
public class SearchMultiThread extends BasicSearch implements Runnable{
    String threadName = "thread";
    public int totalsolutionCount = 0;
    public static int sum = 0;
    int threadCount = 4;
    private Thread t;
    int start = 0;
    int end = 0;
    counter StaticCounter = new counter();
    CT_Latch countdown;
    
    public SearchMultiThread(int searchStart, int searchEnd, int size, String name) {
        
        super(size);
        start = searchStart;
        end = searchEnd;
        threadName = name;
        totalsolutionCount = 0;
    }
    
    //@Override
    public int findAllPlacements(int start, int end){
        //totalsolutionCount = 0;
        solutionCount = 0; 
        totalsolutionCount = 0;
        boolean answer = false;
        while (//!answer && 
            start < end){ //atsakmo ieškojimas/ rekursijos pradžia
            answer = QueenSearch((start++) % n, 0, board);

        }
            return solutionCount / (n-1);
        }

    @Override
    public void run() {
        
        totalsolutionCount += findAllPlacements(start, end);
        StaticCounter.count += totalsolutionCount;
        counter.countList.add(totalsolutionCount);
//        System.out.println(start + " " + end); //testing
//        System.out.println(totalsolutionCount); //testing
//        System.out.println("Exiting " +  threadName ); //testing
        countdown.latch.countDown();
    }

    void start() {
//        System.out.println("Starting " +  threadName ); //testing
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
        }
    }
    
    
}


