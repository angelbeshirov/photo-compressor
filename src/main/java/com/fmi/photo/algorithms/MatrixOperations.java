package com.fmi.photo.algorithms;

import org.apache.commons.math3.linear.*;

public class MatrixOperations {

    public double[][] transpose(double[][] matrix) {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                result[column][row] = matrix[row][column];
            }
        }
        return result;
    }

    public double[][] multiply(double[][] a, double[][] b) {
        double[][] result = null;

        int rows = a.length;
        int columns = b[0].length;

        if (a[0].length == b.length) {
            result = new double[rows][columns];
            for (int row = 0; row < a.length; row++) {
                for (int column = 0; column < b[0].length; column++) {
                    double sum = 0;
                    for (int k = 0; k < a[0].length; k++) {
                        sum += a[row][k] * b[k][column];
                    }
                    result[row][column] = sum;
                }
            }
        }

        return result;
    }

    public int[][] subtractMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; ++i) {
            for (int j = 0; j < matrix1[0].length; ++j) {
                result[i][j] = Math.abs(matrix1[i][j] - matrix2[i][j]);
            }
        }

        return result;
    }

    public double[] getEigenValues(double[][] matrix) {
        RealMatrix realMatrix = new Array2DRowRealMatrix(matrix);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);
        return eigenDecomposition.getRealEigenvalues();
    }

    public double[][] getEigenVectors(double[][] a) {
        RealMatrix realMatrix = new Array2DRowRealMatrix(a);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);
        double[][] result = new double[getEigenValues(a).length][a.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = eigenDecomposition.getEigenvector(i).toArray();
        }
        return result;
    }

    public double[] getSingularValues(double[] a) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = Math.sqrt(a[i]);
        }
        return result;
    }

    public double[] multiplyWithScalar(double[] a, double scalar) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * scalar;
        }

        return result;
    }

    public double[][] generateDiagonalMatrix(double[] array) {
        double[][] result = new double[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i != j) {
                    result[i][j] = 0;
                } else {
                    result[i][j] = array[i];
                }
            }
        }

        return result;
    }
}
