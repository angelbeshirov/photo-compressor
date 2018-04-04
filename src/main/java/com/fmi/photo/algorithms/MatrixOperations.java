package com.fmi.photo.algorithms;

import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.*;

public class MatrixOperations {

    public double[][] convertToDouble(int[][] a) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = a[i][j];
            }
        }
        return result;
    }

    public int[][] convertToInt(double[][] a) {
        int[][] result = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = (int) a[i][j];
            }
        }
        return result;
    }

    public int[][] transpose(int[][] matrix) {
        int[][] result = new int[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                result[column][row] = matrix[row][column];
            }
        }
        return result;
    }

    public double[][] transpose(double[][] matrix) {
        double[][] result = new double[matrix[0].length][matrix.length];
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

    public double[] generateSolution(double[][] a) {
        RealMatrix realMatrix = new Array2DRowRealMatrix(a);
        RealVector realVector = new ArrayRealVector(new double[]{0, 0});
        RealVector solution = new ArrayRealVector();
        ConjugateGradient conjugateGradient = new ConjugateGradient(10000, 3, false);
        RealLinearOperator realLinearOperator = new Array2DRowRealMatrix(a);
        RealVector sol = conjugateGradient.solveInPlace(realLinearOperator, null, realVector, solution);

        return sol.toArray();
    }

    public boolean areOrthogonal(double[] a, double[] b) {
        Vector vec1 = new Vector2D(a);
        Vector vec2 = new Vector2D(b);
        return vec1.dotProduct(vec2) == 0;
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
}