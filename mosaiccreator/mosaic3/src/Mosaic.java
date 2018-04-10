package uk.org.codebox.mosaic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;


public class Mosaic implements Runnable {

    private int IMG_COUNT;
    private String TILES_DIR;
    private String INPUT_IMG;
    private String OUTPUT_IMG;
    private int TILE_WIDTH;
    private int TILE_HEIGHT;
    private int TILE_SCALE;
    private boolean IS_BW;
    private int THREADS;
    private ArrayList<Tile> reuseQueue = new ArrayList<Tile>();
    private final int MAX_QUEUE_SIZE = 3;
    private int SCALED_WIDTH;
    private int SCALED_HEIGHT;

    public Mosaic (String tilesDir, String inputImg, String outputImg, int tileWidth, int tileHeight, int tileScale, boolean isBW, int threads) {
        TILES_DIR = tilesDir;
        INPUT_IMG = inputImg;
        OUTPUT_IMG = outputImg;
        TILE_WIDTH = tileWidth;
        TILE_HEIGHT = tileHeight;
        TILE_SCALE = tileScale;
        IS_BW = isBW;
        THREADS = threads;
        SCALED_WIDTH = TILE_WIDTH / TILE_SCALE;
        SCALED_HEIGHT = TILE_HEIGHT / TILE_SCALE;
    }

    private void log(String msg){
        System.out.println(msg);
    }

    //   public void resizeAndCropImage(String filename) throws IOException {
    public void resizeAndCropImage(File tilesDir) throws IOException {
        File[] files = tilesDir.listFiles();
        int i=0;
        for(File file : files) {
            log("reading " + i);
            javaxt.io.Image currentImage = new javaxt.io.Image(file);
            if(file != null) {
                int currentWidth = currentImage.getWidth();
                int currentHeight = currentImage.getHeight();
                log("got measurements");
                Double ratioWidth = currentWidth / 4.0;
                Double ratioHeight = currentHeight / 3.0;
                // TODO crop from the middle instead of top left corner
                if (ratioWidth >= ratioHeight) { // Limiting dimension: height
                    currentImage.setHeight(240);
                    currentImage.crop(((currentImage.getWidth() - 320)/2), 0, 320, 240);
                } else {                         // Limiting dimension: width
                    currentImage.setWidth(320);
                    currentImage.crop(0, ((currentImage.getHeight() - 240)/2), 320, 240);
                }
                log("saving");
                currentImage.saveAs("images/image" + i + ".jpg");
                i++;
                //if(i==150) break;
            }

        }
    }



    public void run() {
        log("Reading tiles...");
        try {
            final Collection<Tile> tileImages = getImagesFromTiles(new File(TILES_DIR));

            log("Processing input image...");
            File inputImageFile = new File(INPUT_IMG);
            Collection<BufferedImagePart> inputImageParts = getImagesFromInput(inputImageFile);
            final Collection<BufferedImagePart> outputImageParts = Collections.synchronizedSet(new HashSet<BufferedImagePart>());

            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS);

            final AtomicInteger i = new AtomicInteger();
            final int partCount = inputImageParts.size();
            for (final BufferedImagePart inputImagePart : inputImageParts) {
                newFixedThreadPool.execute(new Runnable(){
                    public void run() {
                        Tile bestFitTile = getBestFitTile(inputImagePart.image, tileImages);
                        log(String.format("Matching part %s of %s", i.incrementAndGet(), partCount));
                        outputImageParts.add(new BufferedImagePart(bestFitTile.image, inputImagePart.x, inputImagePart.y));
                    }
                });
            }
            newFixedThreadPool.shutdown();
            newFixedThreadPool.awaitTermination(10000000, TimeUnit.SECONDS);

            log("Writing output image...");
            BufferedImage inputImage = ImageIO.read(inputImageFile);
            int width = inputImage.getWidth();
            int height = inputImage.getHeight();
            BufferedImage output = makeOutputImage(width, height, outputImageParts);
            ImageIO.write(output, "png", new File(OUTPUT_IMG));
            log("FINISHED");
        } catch (IOException e) {
            log("IOException encountered, collage cannot be generated");
        } catch (InterruptedException e ) {
            log ("InterruptedException encountered, collage cannot be generated");
        }
    }

    private BufferedImage makeOutputImage(int width, int height, Collection<BufferedImagePart> parts){
        BufferedImage image = new BufferedImage(width * TILE_SCALE, height * TILE_SCALE, BufferedImage.TYPE_3BYTE_BGR);

        for(BufferedImagePart part : parts){
            BufferedImage imagePart = image.getSubimage(part.x * TILE_SCALE, part.y * TILE_SCALE, TILE_WIDTH, TILE_HEIGHT);
            imagePart.setData(part.image.getData());
        }

        return image;
    }

    private Tile getBestFitTile(BufferedImage target, Collection<Tile> tiles) {
        Tile bestFit = null;
        int bestFitScore = -1;

        for(Tile tile : tiles){
            int score = getScore(target, tile);
            if (score > bestFitScore){
                bestFitScore = score;
                bestFit = tile;
            }
        }
        reuseQueue.add(0, bestFit);
        if (reuseQueue.size() >= MAX_QUEUE_SIZE) reuseQueue.remove(reuseQueue.size() - 1);
        return bestFit;
    }

    private int getScore(BufferedImage target, Tile tile){
        assert target.getHeight() == SCALED_HEIGHT;
        assert target.getWidth() == SCALED_WIDTH;

        int total = 0;
        for(int x = 0; x<SCALED_WIDTH; x++){
            for(int y = 0; y<SCALED_HEIGHT; y++){
                int targetPixel = target.getRGB(x, y);
                Pixel candidatePixel = tile.pixels[x][y];
                int diff = getDiff(targetPixel, candidatePixel);
                int score;
                if (IS_BW){
                    score = 255 - diff;
                } else {
                    score = 255 * 3 - diff;
                }

                total += score;
            }
        }
        for (Tile currentTile: reuseQueue) {
            if (currentTile == tile) total = 0;
        }
        return total;
    }

    private int getDiff(int target, Pixel candidate){
        if (IS_BW){
            return Math.abs(getRed(target) - candidate.r);
        } else {
            return Math.abs(getRed(target) - candidate.r) +
                    Math.abs(getGreen(target) - candidate.g) +
                    Math.abs(getBlue(target) - candidate.b);
        }
    }

    private int getRed(int pixel){
        return  (pixel >>> 16) & 0xff;
    }

    private int getGreen(int pixel){
        return  (pixel >>> 8) & 0xff;
    }

    private int getBlue(int pixel){
        return  pixel & 0xff;
    }

    private Collection<Tile> getImagesFromTiles(File tilesDir) throws IOException{
        Collection<Tile> tileImages = Collections.synchronizedSet(new HashSet<Tile>());
        File[] files = tilesDir.listFiles();
        for(File file : files){
            BufferedImage img = ImageIO.read(file);
            if (img != null){
                tileImages.add(new Tile(img));
                IMG_COUNT += 1;
            } else {
                System.err.println("null image for file " + file.getName());
            }
        }
        return tileImages;
    }

    private Collection<BufferedImagePart> getImagesFromInput(File inputImgFile) throws IOException{
        Collection<BufferedImagePart> parts = new HashSet<BufferedImagePart>();

        BufferedImage inputImage = ImageIO.read(inputImgFile);
        int totalHeight = inputImage.getHeight();
        int totalWidth = inputImage.getWidth();

        int x=0, y=0, w=SCALED_WIDTH, h=SCALED_HEIGHT;
        while(x+w <= totalWidth){
            while(y+h <= totalHeight){
                BufferedImage inputImagePart = inputImage.getSubimage(x, y, w, h);
                parts.add(new BufferedImagePart(inputImagePart, x, y));
                y+=h;
            }
            y=0;
            x+= w;
        }

        return parts;
    }

    public class Tile {
        public Pixel[][] pixels = new Pixel[SCALED_WIDTH][SCALED_HEIGHT];
        public BufferedImage image;

        public Tile(BufferedImage image) {
            this.image = image;
            calcPixels();
        }

        private void calcPixels(){
            for(int x=0; x<SCALED_WIDTH; x++){
                for(int y=0; y<SCALED_HEIGHT; y++){
                    pixels[x][y] = calcPixel(x * TILE_SCALE, y * TILE_SCALE, TILE_SCALE, TILE_SCALE);
                }
            }
        }

        private Pixel calcPixel(int x, int y, int w, int h){
            int redTotal = 0, greenTotal = 0, blueTotal = 0;

            for(int i=0; i<w; i++){
                for(int j=0; j<h; j++){
                    int rgb = image.getRGB(x+i, y+j);
                    redTotal   += getRed(rgb);
                    greenTotal += getGreen(rgb);
                    blueTotal  += getBlue(rgb);
                }
            }
            int count = w*h;
            return new Pixel(redTotal/count, greenTotal/count, blueTotal/count);
        }
    }

    public class BufferedImagePart{
        public BufferedImagePart(BufferedImage image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }

        public BufferedImage image;
        public int x;
        public int y;
    }

    public class Pixel{
        public int r,g,b;

        public Pixel(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        @Override
        public String toString() {
            return r + "." + g + "." + b;
        }
    }


}
