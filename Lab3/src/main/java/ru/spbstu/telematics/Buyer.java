package ru.spbstu.telematics;

import java.util.Arrays;
import java.util.Random;

public class Buyer implements Runnable {
    Cashier cashier; // кассир, который обслуживает покупателя
    int ticketsBought = 0; // количество купленных билетов

    public Buyer(Cashier c) {
        cashier = c;
    }

    private Random r = new Random();
    public boolean getRandomBoolean(float p){
        return r.nextFloat() < p;
    }

    public void run() {
        Hall h = cashier.getHall();
        //Random r = new Random();
        if (h == null)
            return;
        int freeSeats = h.freeSeats();
        while (freeSeats > 0) {
            StringBuilder str = new StringBuilder(Thread.currentThread().getName() + ": want to buy ");
            cashier.startSession();
            freeSeats = cashier.getHall().freeSeats();
            if (freeSeats == 0)
                break;
            int count = r.nextInt(Math.min(freeSeats, 10)) + 1;
            str.append(count).append(" ticket(s): [");
            int i = r.nextInt(h.getHeight());
            int j = r.nextInt(h.getWidth());
            boolean bought = false;

            for (int c = count; c > 0; --c){
                while(bought || !cashier.checkSeat(i, j)) {
                    j++;
                    if (j == h.getWidth()) {
                        i = (i + 1) % h.getHeight();
                        j = 0;
                    }
                    bought = false;
                }
                cashier.select(i, j);
                bought = true;
                str.append("(").append(i).append(";").append(j).append(")");
            }
            str.append("]");
            long reserveTime = System.currentTimeMillis() + 120;
            try {
                Thread.sleep(r.nextInt(120) + 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() < reserveTime){
                if (getRandomBoolean(0.75f)) {
                    boolean res = cashier.tryToBuy();
                    if (res) {
                        System.out.println(str.append(" - successful purchase").toString());
                        ticketsBought += count;
                    } else {
                        cashier.cancelReserve();
                        System.out.println(str.append(" - failed to make a purchase").toString());
                    }
                }
                else{
                    cashier.cancelReserve();
                    System.out.println(str.append(" - refusal to purchase").toString());
                }
            }
            else {
                cashier.cancelReserve();
                System.out.println(str.append(" - reserve time is up!").toString());
            }
            try {
                Thread.sleep(r.nextInt(100) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTicketsBought() {
        return ticketsBought;
    }
}
