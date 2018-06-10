package com.fmi.photo.compression;

import java.io.IOException;

public class Runner {

    public static void main(String[] args) throws IOException {
        PhotoCompressor photoCompressor = new PhotoCompressor("src\\main\\resources\\test\\plant.jpg");
        photoCompressor.compress(0);
        photoCompressor.saveCompressedImage("src\\main\\resources\\results\\newPlant101.jpg", "jpg");
        photoCompressor.generateDifference("src\\main\\resources\\errors\\diffPlant.jpg", "jpg");
    }
}
