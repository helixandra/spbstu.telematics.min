package ru.spbstu.telematics;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Hall {
    public enum Seat {  // состояния места в концертном зале
        Available,
        Reserved,
        Bought
    }

    Seat[][] hallSeats; // места в зале
    ReadWriteLock lock = new ReentrantReadWriteLock();

    Hall(Hall other) {
        hallSeats = other.hallSeats.clone();
    }

    Hall(int width, int height) {
        hallSeats = new Seat[height][width];
        for (Seat[] seats : hallSeats) {
            Arrays.fill(seats, Seat.Available);
        }
    }


    boolean isAvailableForReserve(int i, int j) {
        Seat status;
        try {
            status = hallSeats[i][j];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return status.equals(Seat.Available) /*|| status.equals(Seat.Reserved)*/;
    }

    boolean isAvailableForBuy(int i, int j) {
        Seat status;

        try {
            status = hallSeats[i][j];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return status.equals(Seat.Available) || status.equals(Seat.Reserved);
    }

    void setSeat(int i, int j) {
        try {
            hallSeats[i][j] = Seat.Bought;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    void setReserved(int i, int j) {
        try {
            if (hallSeats[i][j] != Seat.Bought) hallSeats[i][j] = Seat.Reserved;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    void freeSeat(int i, int j) {
        try {
            if (hallSeats[i][j] == Seat.Reserved) hallSeats[i][j] = Seat.Available;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public int getHeight() {
        return (hallSeats == null) ? 0 : hallSeats.length;
    }

    public int getWidth() {
        return (getHeight() == 0) ? 0 : hallSeats[0].length;
    }

    public int freeSeats() {
        int count = 0;
        for (Seat[] row : hallSeats)
            for (Seat s : row)
                if (s.equals(Seat.Available)/*|| s.equals(Seat.Reserved)*/)
                    count++;
        return count;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (Seat[] row : hallSeats) {
            if (first) {
                res.append("[");
                first = false;
            } else {
                res.append("\n[");
            }
            for (Seat s : row)
                switch (s) {
                    case Bought:
                        res.append('*');
                        break;
                    case Reserved:
                        res.append('R');
                        break;
                    case Available:
                        res.append('O');
                        break;
                }
            res.append("]");
        }
        return res.toString();
    }
}
