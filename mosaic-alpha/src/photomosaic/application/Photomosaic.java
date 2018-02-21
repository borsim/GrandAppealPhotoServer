package photomosaic.application;

import photomosaic.data.FileData;
import photomosaic.data.ImageData;
import photomosaic.image.*;
import photomosaic.index.AverageColorIndex;
import photomosaic.utils.progress.ProgressEstimator;
import photomosaic.utils.progress.PeriodicEvent;
import photomosaic.utils.progress.Timer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static photomosaic.image.ColorChannel.*;
import static photomosaic.application.Settings.*;

public class Photomosaic {

    private static ImageData tilesData;
    private static ImageData tinyData;
    private static AverageColorIndex index;

    public static void main(String[] args) throws Exception {
        System.out.println("Loading data...");
        Timer timer = Timer.start();
        tinyData = loadTinyData();
        timer.stop();
        System.out.println("Data loaded in " + TimeUnit.MILLISECONDS.toSeconds(timer.get()) + "s");

        System.out.println("Loading index...");
        index = new AverageColorIndex(30);
        index.readFromFile("index.bin", tinyData.size());
        System.out.println("Index loaded");

        System.out.println("Creating tiles data...");
        tilesData = FileData.forFile("data.bin", TILES_COUNT, TILE_WIDTH, TILE_HEIGHT);
        System.out.println("Data created");

        AwtImage collage = AwtImage.
    }

    private static ImageData loadTinyData() throws IOException {
        ImageData data;
        try (ImageData fileData = FileData.forFile("tinyData.bin", TILES_COUNT, TILE_WIDTH, TILE_HEIGHT) {
            data =
        }
        return data;
    }

}
