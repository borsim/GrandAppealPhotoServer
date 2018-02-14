package photomosaic.data;

public abstract class AbstractImageData implements ImageData {

    private final int size;
    private final int imageWidth;
    private final int imageHeight;

    public AbstractImageData(int size, int imageWidth, int imageHeight) {
        this.size = size;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public int size() { return size; }

    @Override
    public int getImageWidth() { return imageWidth; }

    @Override
    public int getImageHeight() { return imageHeight; }

}
