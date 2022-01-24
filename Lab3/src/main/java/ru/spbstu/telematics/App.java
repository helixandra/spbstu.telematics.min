package ru.spbstu.telematics;

import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args ) throws InterruptedException {
        int nCashiers = 5;
        int width = 20;
        int height = 10;
        final CentralComputer computer = new CentralComputer(width, height, nCashiers);
        List<Buyer> buyerList = new ArrayList<Buyer>();
        List<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < nCashiers; i++) {
            Buyer b = new Buyer(computer.getСashiers()[i]);
            Thread t = new Thread(b);
            t.start();
            threadList.add(t);
            buyerList.add(b);
        }
        new Thread(new Runnable() {
            public void run() {
                while (!computer.soldOut()) {
                    computer.checkHall();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        for(Thread t: threadList)
            t.join();
        computer.checkHall();
        int bought = 0;
        for(Buyer b: buyerList) {
            bought += b.getTicketsBought();
        }
        System.out.println("Size: " + width*height + "; Sold: " + bought);
    }
}

