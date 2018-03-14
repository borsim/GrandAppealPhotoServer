package uk.org.codebox.mosaic;

public class Main {
	public static void main(String args[]) {
		Mosaic mosaic = new Mosaic("tiles", "input.jpg", "output.jpg", 320, 240, 4, false, 2);
		mosaic.run();
	}
}
