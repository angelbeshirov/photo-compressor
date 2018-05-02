package com.fmi.photo.compression;

import com.fmi.photo.algorithms.Converter;
import com.fmi.photo.algorithms.MatrixOperations;
import com.fmi.photo.algorithms.SingularValueDecompositionTemp;

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

    private static double[][] extractMatrix(int index) throws IOException {
        ImageOperations imageOperations = new ImageOperations();

        BufferedImage img = ImageIO.read(new File("src\\main\\resources\\sheep.jpg"));
        int[][] matrixRed = imageOperations.getMatrix(img, ImageOperations.ExtractColor.RED);
        int[][] matrixGreen = imageOperations.getMatrix(img, ImageOperations.ExtractColor.GREEN);
        int[][] matrixBlue = imageOperations.getMatrix(img, ImageOperations.ExtractColor.BLUE);
        int[][] matrixAlpha = imageOperations.getMatrix(img, ImageOperations.ExtractColor.ALPHA);

        int[][] rgbMatrix = imageOperations.getRGBMatrix(matrixRed, matrixGreen, matrixBlue, matrixAlpha);

        if (index == 0) {
            return Converter.convertToDouble(matrixRed);
        } else if (index == 1) {
            return Converter.convertToDouble(matrixGreen);
        } else if (index == 2) {
            return Converter.convertToDouble(matrixBlue);
        } else if (index == 3) {
            return Converter.convertToDouble(matrixAlpha);
        } else {
            return Converter.convertToDouble(rgbMatrix);
        }
    }

    private static double[][] convertUsingSVD(double[][] a, int n) throws IOException {
        SingularValueDecompositionTemp singularValueDecompositionTemp = new SingularValueDecompositionTemp(a);
        singularValueDecompositionTemp.applyCompression(n);
        return singularValueDecompositionTemp.getResult();
    }

    private static int[][] normalizeMatrix(int[][] a) {
        int[][] result = new int[a.length][a[0].length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] < 0) result[i][j] = 0;
                else if (a[i][j] > 255) result[i][j] = 255;
                else {
                    result[i][j] = a[i][j];
                }
            }
        }
        return result;
    }

    public static void main(String args[]) throws IOException {
        MatrixOperations matrixOperations = new MatrixOperations();
        ImageOperations imageOperations = new ImageOperations();
        double red[][] = extractMatrix(0);
        double green[][] = extractMatrix(1);
        double blue[][] = extractMatrix(2);
        double alpha[][] = extractMatrix(3);
        double rgbMtr[][] = extractMatrix(4);

//        int[][] red1 = matrixOperations.convertToInt(red);

        double redCompressed[][] = convertUsingSVD(red, 10);
        double greenCompressed[][] = convertUsingSVD(green, 10);
        double blueCompressed[][] = convertUsingSVD(blue, 10);
        double alphaCompressed[][] = convertUsingSVD(alpha, 10);
        double rgbCompressed[][] = convertUsingSVD(rgbMtr, 10);


        int[][] integerMatrixRed = Converter.convertToInt(redCompressed);
        int[][] integerMatrixGreen = Converter.convertToInt(greenCompressed);
        int[][] integerMatrixBlue = Converter.convertToInt(blueCompressed);
        int[][] integerMatrixAlpha = Converter.convertToInt(alphaCompressed);
        int[][] integerMatrixRGB = Converter.convertToInt(rgbCompressed);

        integerMatrixRed = normalizeMatrix(integerMatrixRed);
        integerMatrixGreen = normalizeMatrix(integerMatrixGreen);
        integerMatrixBlue = normalizeMatrix(integerMatrixBlue);
        integerMatrixAlpha = normalizeMatrix(integerMatrixAlpha);

//        int[][] rgbMatrix = imageOperations.getRGBMatrix(integerMatrixRed, integerMatrixGreen, integerMatrixBlue, integerMatrixAlpha);
        imageOperations.generateImage(integerMatrixRGB, "src\\main\\resources\\newWorld.jpg", "jpg");
    }
}