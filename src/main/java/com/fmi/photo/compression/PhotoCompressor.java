package com.fmi.photo.compression;

import com.fmi.photo.algorithms.MatrixOperations;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PhotoCompressor {
    public static double[][] convertToDouble(int[][] a){
        double[][] result = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                result[i][j] = a[i][j];
            }
        }
        return result;
    }
    public static void main( String[] args ) throws IOException {
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

//        imageOperations.generateImage(imageOperations.getRGBMatrix(matrixRed, matrixGreen, matrixBlue, matrixAlpha), "src\\main\\resources\\test.jpg", "jpg");
        int[][] a = {{2,2,3,7},{1,2,1,4},{0,1,1,6}};
//        for(int i=0;i<a.length;i++)
//        System.out.println(Arrays.toString(a[i]));
//        matrixOperations.test(a);
        int[][] result;
        if(a[0].length > a.length){
            result = matrixOperations.multiply(a, matrixOperations.transpose(a));
        } else {
            result = matrixOperations.multiply(matrixOperations.transpose(a), a);
        }
        for(int i=0;i<result.length;i++){
            System.out.println(Arrays.toString(result[i]));
        }

        System.out.println(Arrays.toString(matrixOperations.getEigenValues(convertToDouble(result))));
        System.out.println();
        double[][] temp = matrixOperations.getEigenVectors(convertToDouble(result));
        for(int i=0;i<temp.length; i++){
            System.out.println(Arrays.toString(temp[i]));
        }
        System.out.println();
    }
}