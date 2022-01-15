package ru.spbstu.telematics;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CentralComputer {

    ReadWriteLock hallLock; // lock на концертный зал
    Hall hall; // концертный зал
    int currentState; // текущее состояние концертного зала
    Cashier[] cashiers; // кассиры
    Integer[] purchaseСount; // массив для подсчета - сколько раз было куплено каждое место

    public CentralComputer(int hallWidth, int hallHeight, int cashiersNum) {
        if (cashiersNum < 1)
            cashiersNum = 1;
        cashiers = new Cashier[cashiersNum];
        for (int i = 0; i < cashiers.length; i++) {
            cashiers[i] = new Cashier(this);
        }
        currentState = 0;
        hall = new Hall(hallWidth, hallHeight);
        hallLock = new ReentrantReadWriteLock();
        purchaseСount = new Integer[hallHeight * hallWidth];
        Arrays.fill(purchaseСount, 0);
    }


    Pair<Hall, Integer> getHall() {
        Lock lock = hallLock.readLock();
        Hall res = null;
        Integer index = null;
        lock.lock();
        try {
            res = new Hall(hall);
            index = currentState;
        } finally {
            lock.unlock();
        }
        return new Pair<Hall, Integer>(res, index);
    }

    boolean buy(Set<Pair<Integer, Integer>> seats, int stateIndex) {  // попытка покупки билетов
        Lock lock = hallLock.writeLock();
        boolean canBuy;
        lock.lock(); // блокируем
        try {
            canBuy = true;
            if (stateIndex != currentState) {  // если состояние зала изменилось, проверяем доступность мест
                for (Pair<Integer, Integer> seat : seats) {
                    if (!hall.isAvailable(seat.getKey(), seat.getValue())) {
                        canBuy = false;
                        break;
                    }
                }
            }
            if (canBuy) { // если можно осуществить покупку, осуществляем - помечаем купленные места
                for (Pair<Integer, Integer> seat : seats) {
                    hall.setSeat(seat.getKey(), seat.getValue());
                    purchaseСount[hall.getWidth() * seat.getKey() + seat.getValue()]++;
                }
                currentState++;
                if (currentState < 0)
                    currentState = 0;
            }
        } finally {
            lock.unlock();
        }
        return canBuy;
    }

    public Cashier[] getСashiers() {
        return cashiers;
    }

    public boolean checkHall() {  // выводит состояние зала
        Lock lock = hallLock.readLock();
        int errors = 0;
        lock.lock();
        try {
            System.out.println("Current hall:\n" + hall);
            int bought = 0;
            for (int i = 0; i < hall.getHeight(); i++) {
                for (int j = 0; j < hall.getWidth(); j++) {
                    int times = purchaseСount[i * hall.getWidth() + j];
                    bought += times;
                    if (times > 1) {
                        errors++;
                        System.out.println("Seat (" + i + "; " + j + ") bought " + times + " times!");
                    }
                }
            }
            System.out.println("Seat bought: " + bought + ".");
            if (errors > 0) {
                System.out.println("Errors: " + errors + ".");
            } else {
                System.out.println("Сorrect execution.");
            }
        } finally {
            lock.unlock();
        }
        return errors > 0;
    }

    public boolean soldOut() {
        return hall.freeSeats() == 0;
    }
}
