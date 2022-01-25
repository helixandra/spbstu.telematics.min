package ru.spbstu.telematics;

import java.util.Arrays;
import java.util.Random;


public class App 
{

    static int[] matrixDims = {500, 1000, 2000};
    static int [][] nThreads = { {2, 4, 6, 8}, {2, 4, 8, 10}, {2, 10, 20, 50}
    };

    private static final Random RANDOM = new Random();

    public static double[][] createA(int size){
        double[][] a = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = RANDOM.nextInt(100) +1;
            }
        }
        return a;
    }

    public static double[] createB(int size){
        double[] b = new double[size];
        for (int i = 0; i < size; i++) {
            b[i] = RANDOM.nextInt(100);
        }
        return b;
    }

    private static void printMas(double [][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.printf("%.2f\t", array[i][j]);
            }
            System.out.println();
        }
    }

    public static void printVect(double[] vect){
        for (int i = 0; i < vect.length; i++) {
            System.out.printf("%.2f\t", vect[i]);
        }
        System.out.println();
    }

    public static void outBeforeExp(double[][] val, double[] res){
        System.out.println("Матрица системы: ");
        printMas(val);
        System.out.println("Вектор свободных значений:");
        printVect(res);
        System.out.println();
    }

    public static double[][] myCopy(double[][] a) {
        double[][] b = new double[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Arrays.copyOf(a[i], a.length);
        }
        return b;
    }

    public static void speedTests(){
        int nTimes = 10;
        int nThreads = 50;
        int size = 1150;
        long time;
        final LinearEquation solver = new LinearEquation();



        for (int threads = 1; threads <= nThreads; ++threads){
            double[][] values = createA(size);
            double[] freeTerms = createB(size);

            double totalTime = 0;
            for (int i = 0; i < nTimes; ++i){
                double[][] valuesCopy = myCopy(values);
                double[] freeTermsCopy = Arrays.copyOf(freeTerms, freeTerms.length);
                double[] result;

                time = System.currentTimeMillis();
                result = solver.solveParallel(valuesCopy, freeTermsCopy, threads);
                totalTime += System.currentTimeMillis() - time;
            }
            System.out.println("Threads: " + threads + "\t Average time: " + totalTime/nTimes + " milliseconds");

        }
    }


    public static void experiments(){
        final LinearEquation solver = new LinearEquation();
        long time;
        double[] result;

        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < 3; ++i) {
                System.out.println("\nEXPERIMENT " + (i + 1));
                int size = matrixDims[i];
                double[][] values = createA(size);
                double[] freeTerms = createB(size);

                double[][] values1 = myCopy(values);
                double[][] values2 = myCopy(values);
                double[][] values3 = myCopy(values);
                double[][] values4 = myCopy(values);

                double[] freeTerms1 = Arrays.copyOf(freeTerms, freeTerms.length);
                double[] freeTerms2 = Arrays.copyOf(freeTerms, freeTerms.length);
                double[] freeTerms3 = Arrays.copyOf(freeTerms, freeTerms.length);
                double[] freeTerms4 = Arrays.copyOf(freeTerms, freeTerms.length);

                System.out.println("Matrix size = " + size + "\nResults:");

                try {
                    time = System.currentTimeMillis();
                    result = solver.solveSerial(values, freeTerms);
                    System.out.println("For 1 thread: " + (System.currentTimeMillis() - time) + " milliseconds");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                time = System.currentTimeMillis();
                result = solver.solveParallel(values1, freeTerms1, nThreads[i][0]);
                System.out.println("For " + nThreads[i][0] + " threads: " + (System.currentTimeMillis() - time) + " milliseconds");

                time = System.currentTimeMillis();
                result = solver.solveParallel(values2, freeTerms2, nThreads[i][1]);
                System.out.println("For " + nThreads[i][1] + " threads: " + (System.currentTimeMillis() - time) + " milliseconds");

                time = System.currentTimeMillis();
                result = solver.solveParallel(values3, freeTerms3, nThreads[i][2]);
                System.out.println("For " + nThreads[i][2] + " threads: " + (System.currentTimeMillis() - time) + " milliseconds");

                time = System.currentTimeMillis();
                result = solver.solveParallel(values4, freeTerms4, nThreads[i][3]);
                System.out.println("For " + nThreads[i][3] + " threads: " + (System.currentTimeMillis() - time) + " milliseconds");
            }
        }
    }

    public static void main( String[] args )
    {
        speedTests();
    }
}
