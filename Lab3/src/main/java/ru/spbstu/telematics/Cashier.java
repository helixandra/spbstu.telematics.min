package ru.spbstu.telematics;

//import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class Cashier {
    CentralComputer computer;  // концерт, на который продаются билеты
    int stateIndex; // текущее состояние концертного зала
    Hall hall; // концертный зал - состояние мест зала
    Set<Pair<Integer, Integer>> toBuy; // билеты, которые хочет приобрести покупатель

    Cashier(CentralComputer c) {
        computer = c;
        stateIndex = -1;
        hall = null;
        toBuy = new HashSet<Pair<Integer, Integer>>();
    }

    private boolean loadHall() {
        if (computer != null) {
            Pair<Hall, Integer> p = computer.getHall();
            hall = p.getKey();
            stateIndex = p.getValue();
            toBuy.clear();
            return false;
        }
        else {
            return true;
        }
    }

    public String showHall() {
        if (hall == null) {
            if (loadHall())
                return "There is no central computer.";
        }
        return hall.toString();
    }

    public boolean checkSeat(int i, int j ) {
        if (hall == null ) {
            if (loadHall())
                return false;
        }
        return hall.isAvailable(i, j);
    }

    public boolean select(int i, int j) {
        if (hall == null)
            if (loadHall())
                return false;
        if (hall.isAvailable(i, j)) {
            toBuy.add(new Pair<Integer, Integer>(i, j));
            hall.setReserved(i, j);
            return true;
        }
        return false;
    }

    public boolean cancelReserve(Pair<Integer, Integer>[] selectedSeats) {
        if (hall == null)
            if (loadHall())
                return false;
        for (Pair<Integer, Integer> seat: selectedSeats) {
            hall.freeSeat(seat.getKey(), seat.getValue());
        }
        return false;
    }

    public boolean tryToBuy() {
        if (computer == null)
            return false;
        boolean res = computer.buy(toBuy, stateIndex);
        return res;
    }

    public boolean startSession() {
        if (computer == null)
            return false;
        loadHall();
        return true;
    }

    public Hall getHall() {
        if (hall == null) {
            loadHall();
        }
        return hall;
    }
}
