package uk.org.codebox.mosaic;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String args[]) throws IOException {
		Mosaic mosaic = new Mosaic("images", "in2.jpg", "finaloutput.png", 320, 240, 8, false, 2);
		mosaic.resizeAndCropImage(new File("tiles4"));
		mosaic.run();
	}
}
