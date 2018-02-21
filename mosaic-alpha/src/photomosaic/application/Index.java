package photomosaic.application;

import photomosaic.data.FileData;
import photomosaic.data.ImageData;
import photomosaic.image.ArrayImage;
import photomosaic.image.AverageColor;
import photomosaic.image.ColorChannel;
import photomosaic.utils.progress.*;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import static photomosaic.application.Settings.*;

public class Index {

    public static void main(String[] args) throws Exception {
        PeriodicEvent periodicLog = new PeriodicEvent(3, TimeUnit.SECONDS);
        periodicLog.update();
        ProgressEstimator progressEstimator = new ProgressEstimator(Timer.start());

        try (
                DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("index.bin")));
                ImageData data = FileData.forFile("data.bin", TILES_COUNT, TILE_WIDTH, TILE_HEIGHT);
                ) {
            ArrayImage buffer = data.createImageBuffer();
            AverageColor averageColor = new AverageColor();

            for (int i = 0; i < data.size(); i++) {
                if (!data.load(i, buffer)) {
                    throw new RuntimeException("Data size is invalid");
                }
                averageColor.calculate(buffer);

                if (periodicLog.update()) {
                    double progress = i / (double) data.size();
                    long estimatedSeconds = TimeUnit.MILLISECONDS.toSeconds(progressEstimator.estimateRemainingTime(progress));
                    System.out.format("Progress = %.3f, remaining time = %ds\n", progress, estimatedSeconds);
                }
            }
        }
    }

}
