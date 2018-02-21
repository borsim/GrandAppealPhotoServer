package photomosaic.data;

import photomosaic.image.ArrayImage;
import photomosaic.image.ColorChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileData extends StreamImageData {

    private final int imageSizeBytes;
    private final RandomAccessFile file;

    public FileData(RandomAccessFile file, int size, int imageWidth, int imageHeight) {
        super(size, imageWidth, imageHeight);
        this.file = file;
        this.imageSizeBytes = ColorChannel.values().length * imageWidth * imageHeight;
    }

    public static FileData forFile(String fileName, int size, int imageWidth, int imageHeight) throws FileNotFoundException {
        return forFile(new File(fileName), size, imageWidth, imageHeight);
    }

    public static FileData forFile(File file, int size, int imageWidth, int imageHeight) throws FileNotFoundException {
        return new FileData(new RandomAccessFile(file, "r"), size, imageWidth, imageHeight);
    }

    @Override
    protected void changePosition(int index) throws IOException {
        file.seek(index * (long) imageSizeBytes);
    }

    @Override
    protected void readTo(ArrayImage destination) throws IOException {
        destination.readFrom(file);
    }

    @Override
    public void close() throws IOException {
        file.close();
    }

}
