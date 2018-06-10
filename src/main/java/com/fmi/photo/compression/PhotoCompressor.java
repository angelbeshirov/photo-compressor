package com.fmi.photo.compression;

import com.fmi.photo.algorithms.Converter;
import com.fmi.photo.algorithms.MatrixOperations;
import com.fmi.photo.algorithms.SingularValueDecomposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PhotoCompressor {

    private final BufferedImage bufferedImageNormal;
    private BufferedImage bufferedImageCompressed;
    public PhotoCompressor(String path) throws IOException {
        bufferedImageNormal = ImageIO.read(new File(path));
        bufferedImageCompressed = ImageIO.read(new File(path));
    }

    private double[][] convertUsingSVD(double[][] a, int n, String fileName) {
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(a);
        ImageOperations imageOperations = new ImageOperations();
//        imageOperations.writeSingularValues(singularValueDecomposition.getS(), fileName);
        singularValueDecomposition.applyCompression(n);
        return singularValueDecomposition.getResult();
    }

    public void compress(int factor) {
        MatrixOperations matrixOperations = new MatrixOperations();
        ImageOperations imageOperations = new ImageOperations();
        double red[][] = Converter.convertToDouble(imageOperations.getMatrix(bufferedImageNormal, ImageOperations.ExtractColor.RED));
        double green[][] = Converter.convertToDouble(imageOperations.getMatrix(bufferedImageNormal,ImageOperations.ExtractColor.GREEN));
        double blue[][] = Converter.convertToDouble(imageOperations.getMatrix(bufferedImageNormal, ImageOperations.ExtractColor.GREEN));
        int alpha[][] = imageOperations.getMatrix(bufferedImageNormal, ImageOperations.ExtractColor.ALPHA);

        double redCompressed[][] = convertUsingSVD(red, factor, "src//main//resources//redSingularValuesForPlant.txt");
        double greenCompressed[][] = convertUsingSVD(green, factor,"src//main//resources//greenSingularValuesForPlant.txt");
        double blueCompressed[][] = convertUsingSVD(blue, factor, "src//main//resources//blueSingularValuesForPlant.txt");
//        double alphaCompressed[][] = convertUsingSVD(alpha, factor);

        int[][] integerMatrixRed = Converter.convertToInt(redCompressed);
        int[][] integerMatrixGreen = Converter.convertToInt(greenCompressed);
        int[][] integerMatrixBlue = Converter.convertToInt(blueCompressed);
//        int[][] integerMatrixAlpha = Converter.convertToInt(alphaCompressed);

        matrixOperations.normalizeMatrix(integerMatrixRed);
        matrixOperations.normalizeMatrix(integerMatrixGreen);
        matrixOperations.normalizeMatrix(integerMatrixBlue);
//        matrixOperations.normalizeMatrix(integerMatrixAlpha);

        int[][] rgbMatrix = imageOperations.getRGBMatrix(integerMatrixRed, integerMatrixGreen, integerMatrixBlue, alpha);
        this.bufferedImageCompressed = imageOperations.generateImage(rgbMatrix);
    }

    public void saveCompressedImage(String location, String extension) throws IOException {
        ImageOperations imageOperations = new ImageOperations();
        imageOperations.writeImage(this.bufferedImageCompressed, location, extension);
    }

    public void generateDifference(String path, String extension) throws IOException {
        ImageOperations imageOperations = new ImageOperations();
        MatrixOperations matrixOperations = new MatrixOperations();

        int redNormal[][] = imageOperations.getMatrix(bufferedImageNormal, ImageOperations.ExtractColor.RED);
        int greenNormal[][] = imageOperations.getMatrix(bufferedImageNormal,ImageOperations.ExtractColor.GREEN);
        int blueNormal[][] = imageOperations.getMatrix(bufferedImageNormal, ImageOperations.ExtractColor.GREEN);
        int alphaNormal[][] = imageOperations.getMatrix(bufferedImageNormal, ImageOperations.ExtractColor.ALPHA);

        int redCompressed[][] = imageOperations.getMatrix(bufferedImageCompressed, ImageOperations.ExtractColor.RED);
        int greenCompressed[][] = imageOperations.getMatrix(bufferedImageCompressed,ImageOperations.ExtractColor.GREEN);
        int blueCompressed[][] = imageOperations.getMatrix(bufferedImageCompressed, ImageOperations.ExtractColor.GREEN);
        int alphaCompressed[][] = imageOperations.getMatrix(bufferedImageCompressed, ImageOperations.ExtractColor.ALPHA);

        int[][] diffRed = matrixOperations.subtractMatrix(redCompressed, redNormal);
        int[][] diffGreen = matrixOperations.subtractMatrix(greenCompressed, greenNormal);
        int[][] diffBlue = matrixOperations.subtractMatrix(blueCompressed, blueNormal);
        int[][] diffAlpha = matrixOperations.subtractMatrix(alphaCompressed, alphaNormal);

        int[][] rgbMatrixDiff = imageOperations.getRGBMatrix(diffRed, diffGreen, diffBlue, diffAlpha);

        imageOperations.writeImage(imageOperations.generateImage(rgbMatrixDiff), path, extension);
    }
}