package com.fmi.photo.algorithms;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MatrixOperations {

    public int[][] transpose(int[][] matrix) {
        int[][] result = new int[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                result[column][row] = matrix[row][column];
            }
        }
        return result;
    }

    public int[][] multiply(int[][] a, int[][] b) {
        int[][] result = null;

        int rows = a.length;
        int columns = b[0].length;

        if (a[0].length == b.length) {
            result = new int[rows][columns];
            for (int row = 0; row < a.length; row++) {
                for (int column = 0; column < b[0].length; column++) {
                    int sum = 0;
                    for (int k = 0; k < a[0].length; k++) {
                        sum += a[row][k] * b[k][column];
                    }
                    result[row][column] = sum;
                }
            }
        }


        return result;
    }

    public double[] getEigenValues(double[][] matrix){
        RealMatrix realMatrix = new Array2DRowRealMatrix(matrix);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);
        return eigenDecomposition.getRealEigenvalues();
    }

    public double[][] getEigenVectors(double[][] a) {
        RealMatrix realMatrix = new Array2DRowRealMatrix(a);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);
        double[][] result = new double[getEigenValues(a).length][a.length];
        for (int i = 0; i < result.length; i++) {
            RealVector realVector = eigenDecomposition.getEigenvector(i);
            double[] temp = realVector.toArray();
            result[i] = temp;
        }
        return result;
    }
}
