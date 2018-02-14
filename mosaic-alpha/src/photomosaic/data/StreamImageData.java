package photomosaic.data;

import photomosaic.image.ArrayImage;

import java.io.EOFException;
import java.io.IOException;

public abstract class StreamImageData extends AbstractImageData {

    private int currentIndex = -1;

    public StreamImageData(int size, int imageWidth, int imageHeight) {
        super(size, imageWidth, imageHeight);
    }

    @Override
    public boolean load(int index, ArrayImage destination) throws IOException {
        if (currentIndex != index) {
            changePosition(index);
            currentIndex = index;
        }

        currentIndex++;
        try {
            readTo(destination);
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    /**
     * Move the current data pointer into given position;
     */
    protected abstract void changePosition(int index) throws IOException;

    /**
     * Reads the next image from data
     * @param destination
     */
    protected abstract void readTo(ArrayImage destination) throws IOException;

}
