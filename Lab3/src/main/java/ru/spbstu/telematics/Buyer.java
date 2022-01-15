package ru.spbstu.telematics;

import java.util.Arrays;
import java.util.Random;

public class Buyer implements Runnable {
    Cashier cashier; // кассир, который обслуживает покупателя
    int ticketsBought = 0; // количество купленных билетов

    public Buyer(Cashier c) {
        cashier = c;
    }

    public void run() {
        Hall h = cashier.getHall();
        Random r = new Random();
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

            Pair<Integer, Integer>[] selectedSeats = new Pair[count];
            Arrays.fill(selectedSeats, new Pair(0,0));
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
                selectedSeats[count-c].setKey(i);
                selectedSeats[count-c].setValue(j);
                str.append("(").append(i).append(";").append(j).append(")");
            }
            str.append("]");
            try {
                Thread.sleep(r.nextInt(50) + 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean res = cashier.tryToBuy();
            if (res) {
                System.out.println(str.append(" - successful purchase").toString());
                ticketsBought += count;
                try {
                    Thread.sleep(r.nextInt(100) + 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                cashier.cancelReserve(selectedSeats);
                System.out.println(str.append(" - failed to make a purchase").toString());
            }
        }
    }

    public int getTicketsBought() {
        return ticketsBought;
    }
}
