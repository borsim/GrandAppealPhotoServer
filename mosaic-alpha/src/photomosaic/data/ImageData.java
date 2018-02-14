package photomosaic.data;

import photomosaic.image.ArrayImage;
import photomosaic.image.Image;

import java.io.Closeable;
import java.io.IOException;

public interface ImageData extends Closeable {

    default boolean load(int index, ArrayImage destination) throws IOException {
        Image buffer = get(index);
        if (buffer == null) {
            return false;
        }
        destination.copyPixels(buffer);
        return true;
    }

    default Image get(int index) throws IOException {
        ArrayImage buffer = createImageBuffer();
        return load(index, buffer) ? buffer : null;
    }

    int size();

    int getImageWidth();

    int getImageHeight();

    default ArrayImage createImageBuffer() {
        return new ArrayImage(getImageWidth(), getImageHeight());
    }
}
