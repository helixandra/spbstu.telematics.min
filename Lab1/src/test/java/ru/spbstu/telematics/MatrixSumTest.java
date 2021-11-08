package ru.spbstu.telematics;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;


public class MatrixSumTest {

     private static final int [][] mrx45_1 = {{ 5,  8, 3,  15, 14},
			                                  { 1,  9,  4, 16,  7},
 			                                  { 3, 17, 2,  6, 11},
 			                                  { 13,  25, 12,  4,  5}};
		
    private static final int [][] mrx45_2 = {{ 5, 14, 12, 4, 26},
                                               {9, 14, 21, 16, 4},
                                               {23, 16, 26, 4, 23},
                                               {17, 28, 28, 1, 9}};

    private static final int [][] mrx34_1 = {{ 2,  18, 0,  5},
			                                       { 13,  28,  5, 8},
 			                                       { 20, 5, 9,  19}};

    private static final int [][] mrx34_2 = {{ 23, 4, 2, 16},
                                               {21, 13, 15, 22},
                                               {8, 30, 9, 18}};

    private static final int [][] mrx44_1 = {{ 5,  19, 7,  1},
			                                  { 5,  0,  11, 27},
 			                                  { 1, 4, 24,  27},
 			                                  { 3,  24, 24,  2}};
		
    private static final int [][] mrx44_2 = {{ 27, 26, 28, 2},
                                               {20, 25, 14, 28},
                                               {0, 23, 5, 27},
                                               {11, 2, 8, 3}};  
    

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before MatrixSumTest.class");
    }

    @AfterClass
    public  static void afterClass() {
        System.out.println("After MatrixSumTest.class");
    }
  
    @Test
    public void testMatrix4_5()
    {
      System.out.println("Test 4x5 matrix - testMatrix4_5()");
        final int[][] expected = {{10,	22,	15,	19,	40},
                                  {10,	23,	25,	32,	11},
                                  {26,	33,	28,	10,	34},
                                  {30,	53,	40,	5,	14}};
        
        final int[][] actual = MatrixSum.matrixSum(mrx45_1, mrx45_2);
  
      // Assert
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void testMatrix3_4()
    {
      System.out.println("Test 3x4 matrix - testMatrix3_4()");
        final int[][] expected = {{25, 22,	2,	21},
                                  {34,	41,	20,	30},
                                  {28,	35,	18,	37}};

        
        final int[][] actual = MatrixSum.matrixSum(mrx34_1, mrx34_2);
  
      // Assert
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void testMatrix4_4()
    {
      System.out.println("Test 4x4 matrix - testMatrix4_4()");
        final int[][] expected = {{32,	45,	35,	3},
                                  {25,	25,	25,	55},
                                  {1,	27,	29,	54},
                                  {14,	26,	32,	5}};

        
        final int[][] actual = MatrixSum.matrixSum(mrx44_1, mrx44_2);
  
      // Assert
        assertTrue(Arrays.deepEquals(expected, actual));
    }
}