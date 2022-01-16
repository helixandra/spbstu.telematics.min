package ru.spbstu.telematics;

import java.util.Arrays;
import java.util.List;


class Solver implements Runnable{
    static final double eps = 1E-5;

    private double[][] values;
    private double[] res;
    private int from;
    private int to;
    public List<Boolean> synStrList;

    public Solver(double[][] values, double[] res, int from, int to, List<Boolean> synStrList) {
        this.values = values;
        this.res = res;
        this.from = from;
        this.to = to;
        this.synStrList = synStrList;
    }

    @Override
    public void run() {
        int n = res.length;
        double[] substract = null;
        for (int c = 0; c < n; ++c) {
            if (c < from)
                while (!synStrList.get(c)){}
            else if (c >= from && c <= to) {
                synStrList.set(c, true);
            }
            else break;

            if (synStrList.get(c)) {
                substract = Arrays.copyOf(values[c], n);

                int i = c + 1;
                if (c < from) i = from;
                for (; i <= to; i++) {
                    double alpha = values[i][c] / substract[c];
                    res[i] -= alpha * res[c];
                    for (int j = c; j < n; j++) {
                        values[i][j] -= alpha * values[c][j];
                    }
                }
            }
        }
    }
}
