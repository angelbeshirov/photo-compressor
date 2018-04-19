package com.fmi.photo.compression;

import com.fmi.photo.algorithms.MatrixOperations;
import org.apache.commons.math3.linear.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class PhotoCompressor {

    public static void print2DMatrix(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(Arrays.toString(a[i]));
        }
    }

    public static void print2DMatrix(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(Arrays.toString(a[i]));
        }
    }

    public static void main(String[] args) throws IOException {
        ImageOperations imageOperations = new ImageOperations();
        MatrixOperations matrixOperations = new MatrixOperations();

        BufferedImage img = ImageIO.read(new File("src\\main\\resources\\sheep.jpg"));
        int[][] matrixRed = imageOperations.getMatrix(img, ImageOperations.ExtractColor.RED);
        int[][] matrixGreen = imageOperations.getMatrix(img, ImageOperations.ExtractColor.GREEN);
        int[][] matrixBlue = imageOperations.getMatrix(img, ImageOperations.ExtractColor.BLUE);
        int[][] matrixAlpha = imageOperations.getMatrix(img, ImageOperations.ExtractColor.ALPHA);
        imageOperations.writeToFile(matrixRed, "src\\main\\resources\\red.txt");
        imageOperations.writeToFile(matrixGreen, "src\\main\\resources\\green.txt");
        imageOperations.writeToFile(matrixBlue, "src\\main\\resources\\blue.txt");
        imageOperations.writeToFile(matrixAlpha, "src\\main\\resources\\alpha.txt");

        int[][] rgbMatrix = imageOperations.getRGBMatrix(matrixRed, matrixGreen, matrixBlue, matrixAlpha);
        imageOperations.generateImage(rgbMatrix, "src\\main\\resources\\test_sheep.jpg", "jpg");
        imageOperations.writeToFile(rgbMatrix, "src\\main\\resources\\REAL.txt");
//        int[][] a = {{2, 0}, {1, 2}, {0, 1}};
        int a[][] = imageOperations.getRGBMatrix(matrixRed, matrixGreen, matrixBlue, matrixAlpha);
//        for(int i=0;i<a.length;i++)
//        System.out.println(Arrays.toString(a[i]));
//        matrixOperations.test(a);
        int[][] result;
        if (a[0].length > a.length) {
            result = matrixOperations.multiply(a, matrixOperations.transpose(a));
        } else {
            result = matrixOperations.multiply(matrixOperations.transpose(a), a);
        }

        System.out.println("Transposed and multiplied matrix is:");
//        print2DMatrix(matrixOperations.multiply(matrixOperations.transpose(a), a));

//        System.out.println("Eigenvalues are:");
        double[] eigenValues = matrixOperations.getEigenValues(matrixOperations.convertToDouble(result));
//        System.out.println(Arrays.toString(eigenValues));
//        System.out.println("Eigenvectors are:");
        double[][] eigenVectors = matrixOperations.getEigenVectors(matrixOperations.convertToDouble(result));
//        for (int i = 0; i < eigenVectors.length; i++) {
//            System.out.println(Arrays.toString(eigenVectors[i]));
//        }
//        System.out.println();

        double[][] convertedF = matrixOperations.convertToDouble(a);
        double[][] Av = matrixOperations.generateAvis(convertedF, eigenVectors);

        System.out.println("AV: ");
        print2DMatrix(Av);


//        System.out.println("The two vectors " + (matrixOperations.areOrthogonal(eigenVectors[0], eigenVectors[1]) ? "are " : "are not ") + "orthogonal");


//        System.out.println("Second multiplication");
//        System.out.println(Arrays.toString(Av2));
//        ui = 1/singular[i]*Avi

        double[] singularValues = matrixOperations.getSingularValues(eigenValues);

//        System.out.println("Singular values are:");
//        System.out.println(Arrays.toString(singularValues));

            double[][] Ui = matrixOperations.generateUis(Av, singularValues);
            System.out.println("UI : ");
            print2DMatrix(Ui);
            double[][] U = matrixOperations.transpose(Ui);

//        double[][] U = new double[u1.length][2];
//
//        for(int i = 0; i < u1.length; i++){
//            U[i][0] = u1[i];
//            U[i][1] = u2[i];
//        }

//        System.out.println("U = ");
//        print2DMatrix(U);

        double[][] S = matrixOperations.generateDiagonalMatrix(singularValues);
//        System.out.println("Singular matrix: ");
//        print2DMatrix(S);

        double[][] V = matrixOperations.transpose(eigenVectors);
//        System.out.println("V: ");
//        print2DMatrix(V);

//        System.out.println("FINAL: ");
//        print2DMatrix(matrixOperations.multiply(U, matrixOperations.multiply(S, V)));

        double[][] finalResult = matrixOperations.multiply(U, matrixOperations.multiply(S, V));
        imageOperations.writeToFile(finalResult, "src\\main\\resources\\TEST.txt");

//        TODO
//        Find a way to solve nxm system, currently this will work for nxn
//        double[] u3 = matrixOperations.generateSolution(new double[][] {u1,u2});

//        TODO
//            Everything needs to be separated by functionality
//            The different vectors have to be generated in more generic way for n
//            The generation of the different multiplications i.e. Av1, Av2 needs to be done to work for n vectors
//


    }
}