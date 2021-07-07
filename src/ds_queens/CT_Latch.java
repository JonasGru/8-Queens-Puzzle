/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ds_queens;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Jonas
 */
public class CT_Latch {
    static public CountDownLatch latch = new CountDownLatch(4);
    
    public CT_Latch(int latchCount){
        latch = new CountDownLatch(latchCount);
    }
}
