package com.fmi.photo.compression;

import com.fmi.photo.algorithms.MatrixOperations;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

        BufferedImage img = ImageIO.read(new File("src\\main\\resources\\sparrow.jpg"));
        int[][] matrixRed = imageOperations.getMatrix(img, ImageOperations.ExtractColor.RED);
        int[][] matrixGreen = imageOperations.getMatrix(img, ImageOperations.ExtractColor.GREEN);
        int[][] matrixBlue = imageOperations.getMatrix(img, ImageOperations.ExtractColor.BLUE);
        int[][] matrixAlpha = imageOperations.getMatrix(img, ImageOperations.ExtractColor.ALPHA);
        imageOperations.writeToFile(matrixRed, "src\\main\\resources\\red.txt");
        imageOperations.writeToFile(matrixGreen, "src\\main\\resources\\green.txt");
        imageOperations.writeToFile(matrixBlue, "src\\main\\resources\\blue.txt");
        imageOperations.writeToFile(matrixAlpha, "src\\main\\resources\\alpha.txt");

        imageOperations.generateImage(imageOperations.getRGBMatrix(matrixRed, matrixGreen, matrixBlue, matrixAlpha), "src\\main\\resources\\test.jpg", "jpg");
        int[][] a = {{2, 0}, {1, 2}, {0, 1}};
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
        print2DMatrix(matrixOperations.multiply(matrixOperations.transpose(a), a));

        System.out.println("Eigenvalues are:");
        double[] eigenValues = matrixOperations.getEigenValues(matrixOperations.convertToDouble(result));
        System.out.println(Arrays.toString(eigenValues));
        System.out.println("Eigenvectors are:");
        double[][] eigenVectors = matrixOperations.getEigenVectors(matrixOperations.convertToDouble(result));
        for (int i = 0; i < eigenVectors.length; i++) {
            System.out.println(Arrays.toString(eigenVectors[i]));
        }
        System.out.println();


        System.out.println("The two vectors " + (matrixOperations.areOrthogonal(eigenVectors[0], eigenVectors[1]) ? "are " : "are not ") + "orthogonal");

        double[] Av1 = matrixOperations.transpose(matrixOperations.multiply(matrixOperations.convertToDouble(a), matrixOperations.transpose((new double[][]{eigenVectors[0]}))))[0];
        System.out.println("First multiplication");
        System.out.println(Arrays.toString(Av1));

        double[] Av2 = matrixOperations.transpose(matrixOperations.multiply(matrixOperations.convertToDouble(a), matrixOperations.transpose(new double[][]{eigenVectors[1]})))[0];
        System.out.println("Second multiplication");
        System.out.println(Arrays.toString(Av2));

        //ui = 1/singular[i]*Avi

        double[] singularValues = matrixOperations.getSingularValues(eigenValues);

        System.out.println("Singular values are:");
        System.out.println(Arrays.toString(singularValues));


        double[] u1 = matrixOperations.multiplyWithScalar(Av1, 1.0 / singularValues[0]);

        double[] u2 = matrixOperations.multiplyWithScalar(Av2, 1.0 / singularValues[1]);

        System.out.println("The Ui vectors are:");
        System.out.println(Arrays.toString(u1));
        System.out.println(Arrays.toString(u2));

        System.out.println("Generating the 3rd orthogonal vector:");


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