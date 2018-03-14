package uk.org.codebox.mosaic;

import java.io.IOException;

public class Main {
	public static void main(String args[]) throws IOException {
		Mosaic mosaic = new Mosaic("tiles", "input.jpg", "output.jpg", 320, 240, 4, false, 2);
		mosaic.resizeAndCropImage("CONTOUR_WORKS.png");
	}
}
