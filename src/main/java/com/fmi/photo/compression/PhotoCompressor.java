package com.fmi.photo.compression;

import com.fmi.photo.algorithms.MatrixOperations;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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

    private static double[][] extractMatrix(int index) throws IOException {
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
//        imageOperations.generateImage(rgbMatrix, "src\\main\\resources\\test_sheep.jpg", "jpg");
//        imageOperations.writeToFile(rgbMatrix, \"src\\\\main\\\\resources\\REAL.txt");

        if(index == 0){
            return matrixOperations.convertToDouble(matrixRed);
        } else if (index == 1) {
            return matrixOperations.convertToDouble(matrixGreen);
        } else if (index == 2) {
            return matrixOperations.convertToDouble(matrixBlue);
        } else if (index == 3) {
            return matrixOperations.convertToDouble(matrixAlpha);
        } else {
            return matrixOperations.convertToDouble(rgbMatrix);
        }
    }

    public static int[][] convertUsingSVD(double[][] a){
        MatrixOperations matrixOperations = new MatrixOperations();
//        double[][] a = matrixOperations.convertToDouble(matrix);
        double[][] u = matrixOperations.getU(a);
        double[][] sigma = matrixOperations.getSignma(a);
        double[][] v = matrixOperations.getV(a);
        System.out.println("This is U:");
//        print2DMatrix(u);
        System.out.println("This is sigma:");
//        print2DMatrix(sigma);
        System.out.println("This is V:");
//        print2DMatrix(v);

        System.out.println("Final result is here:");

        double[][] sigma2 = matrixOperations.compressing(sigma, 1000000.0);
        print2DMatrix(sigma2);
        double[][] finalResult = matrixOperations.multiply(u, matrixOperations.multiply(sigma2, matrixOperations.transpose(v)));
//        print2DMatrix(finalResult);
        double[][] finalResult2 = matrixOperations.roundToInt(finalResult);

        System.out.println("Rounded result:");
//        print2DMatrix(finalResult2);

        System.out.println("Integer matrix:");
        int[][] result = matrixOperations.convertToInt(finalResult2);

        return result;
    }

    public static void trySvd() throws IOException {
        ImageOperations imageOperations = new ImageOperations();
//        int[][] convertedRed = convertUsingSVD(extractMatrix(0));
//        int[][] convertedGreen = convertUsingSVD(extractMatrix(1));
//        int[][] convertedBlue = convertUsingSVD(extractMatrix(2));
//        int[][] convertedAlpha = convertUsingSVD(extractMatrix(3));


        int[][] convertedRgbMatrix = convertUsingSVD(extractMatrix(4));

        imageOperations.generateImage(convertedRgbMatrix, "src\\main\\resources\\test_sheep_newCompression.jpg", "jpg");
    }

    public static void main(String[] args) throws IOException {
        ImageOperations imageOperations = new ImageOperations();
        MatrixOperations matrixOperations = new MatrixOperations();

//        double[][] a = {{2, 0}, {1, 2}, {0, 1}, {22, 0}, {11, 2}, {41, 12}};
        double a[][] = extractMatrix(4);
        //for(int i=0;i<a.length;i++)
        //System.out.println(Arrays.toString(a[i]));
        // matrixOperations.test(a);
        double[][] result;
        if (a[0].length > a.length) {
            result = matrixOperations.multiply(a, matrixOperations.transpose(a));
        } else {
            result = matrixOperations.multiply(matrixOperations.transpose(a), a);
        }
        int resultLen = result.length;
//        System.out.println("Transposed and multiplied matrix is:");
//        print2DMatrix(result);

        //System.out.println("Eigenvalues are:");

        double[] eigenValues = matrixOperations.getEigenValues(result);
//        System.out.println(Arrays.toString(eigenValues));
//        System.out.println("Eigenvectors are:");
        double[][] eigenVectors = matrixOperations.getEigenVectors(result);
//        print2DMatrix(eigenVectors);
 //       System.out.println();

        double[][] Av = matrixOperations.generateAvis(a, eigenVectors);

//        System.out.println("AV: ");
//        print2DMatrix(Av);


//        System.out.println("The two vectors " + (matrixOperations.areOrthogonal(eigenVectors[0], eigenVectors[1]) ? "are " : "are not ") + "orthogonal");


//        System.out.println("Second multiplication");
//        System.out.println(Arrays.toString(Av2));
//        ui = 1/singular[i]*Avi

        double[] singularValues = matrixOperations.getSingularValues(eigenValues);

//        System.out.println("Singular values are:");
//        System.out.println(Arrays.toString(singularValues));

            double[][] Ui = matrixOperations.generateUis(Av, singularValues);

           // double[][] U = matrixOperations.transpose(Ui);

           // System.out.println("UI : ");
          //  print2DMatrix(U);
//
        double[][] S = matrixOperations.generateDiagonalMatrix(singularValues);
        System.out.println("Singular matrix: ");
//        print2DMatrix(S);
//      raya: NOTE that this is actually V transposed
        double[][] V = matrixOperations.transpose(eigenVectors);
        System.out.println("V: ");
//        print2DMatrix(V);
//
        System.out.println("FINAL: ");
//        print2DMatrix(matrixOperations.multiply(U, matrixOperations.multiply(S, V)));

        // raya: this is applicable only when columns are more than rows: V(transposed)*S*U
        // when rows are more that columns Ui(transposed a.k.a. U)*S*V(transposed)
        double[][] finalResult = matrixOperations.multiply(V, matrixOperations.multiply(S, Ui));
        //        double[][] finalResult = matrixOperations.multiply(U, matrixOperations.multiply(S, V));
        imageOperations.writeToFile(finalResult, "src\\main\\resources\\TEST.txt");
//        print2DMatrix(finalResult);

//        TODO
//        Find a way to solve nxm system, currently this will work for nxn
//        double[] u3 = matrixOperations.generateSolution(new double[][] {u1,u2});

//        TODO
//            Everything needs to be separated by functionality
//            The different vectors have to be generated in more generic way for n
//            The generation of the different multiplications i.e. Av1, Av2 needs to be done to work for n vectors
//

//        print2DMatrix(result);
        int[][] resultInt = matrixOperations.convertToInt(finalResult);
        imageOperations.generateImage(resultInt, "src\\main\\resources\\test_sheep_compressed.jpg", "jpg");

    }
}