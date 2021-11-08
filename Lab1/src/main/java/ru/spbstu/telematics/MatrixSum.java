package ru.spbstu.telematics;

import java.util.Scanner;


public class MatrixSum 
{
    public static void main( String[] args )
    {
        //get num of cols and rows
        System.out.println("Введите размерность матриц.");
        System.out.print("Количество строк: ");
        Scanner in = new Scanner(System.in);
        int rows = in.nextInt();
        while (rows <= 0){
            System.out.print("Количество строк должно быть больше 0. Введите новое число: ");
            rows = in.nextInt();
        }
        System.out.print("Количество столбцов: ");
        int cols = in.nextInt();
        while (cols <= 0){
            System.out.print("Количество столбцов должно быть больше 0. Введите новое число: ");
            cols=in.nextInt();
        }
        in.close();

        //fill matrices
        int[][] matrix1 = new int[rows][cols];
        int[][] matrix2 = new int[rows][cols];
        for (int i=0; i<matrix1.length; ++i)
            for(int j=0; j<matrix1[i].length; ++j){
                matrix1[i][j]=(int) (Math.random()*41)-10;
                matrix2[i][j]=(int) (Math.random()*41) - 10;
            }
    
        

        //get sum
        MatrixSum.printRes(matrix1, matrix2, matrixSum(matrix1, matrix2));
        
    }

    public static int[][]  matrixSum (int[][] mrx1, int[][] mrx2){
        int [][] resMrx = new int [mrx1.length][mrx1[0].length];
        for(int i=0; i<mrx1.length; ++i)
            for (int j=0;j<mrx1[i].length; ++j)
                    resMrx[i][j]=mrx1[i][j]+mrx2[i][j];

        return resMrx;
    }

    public static void printRes(int[][] mrx1, int[][] mrx2, int[][] res){
        System.out.println("\nМатрицы для суммирования: ");
        for (int i = 0; i < mrx1.length; ++i)
	    {
		    for (int j = 0; j < mrx1[i].length; ++j)
                System.out.print(mrx1[i][j]+"\t");
		    System.out.print("\t\t");
		    for (int j = 0; j < mrx2[i].length; ++j)
		    	System.out.print(mrx2[i][j]+"\t");
		    System.out.println();
	    }

        System.out.println("\nРезультат суммирования: ");
        for(int i=0; i<res.length; ++i){
            for (int j=0;j<res[i].length; ++j)
                    System.out. print(res[i][j]+"\t");
            System.out.println();
        }
                
    }
}
