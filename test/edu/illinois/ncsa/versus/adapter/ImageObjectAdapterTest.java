package edu.illinois.ncsa.versus.adapter;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;

public class ImageObjectAdapterTest {

	@Test
	public void testGetPixels() {
		
		ImageObjectAdapter adapter = new ImageObjectAdapter();

		try {
			adapter.load(new File("data/Tulips2.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Faild loading file");
		}
		
		double[][][] pixels = adapter.getRGBPixels();
		
		if (pixels.length < 1 || pixels[0].length < 1 || pixels[0][0].length < 1) {
			fail("Loaded image has missing pixels");
		}
	}

	@Test
	public void testLoadFile() {

		ImageObjectAdapter adapter = new ImageObjectAdapter();

		File file = new File("data/Tulips.jpg");
		
		System.out.print("Absolute path: " + file.getAbsolutePath());
		
		try {
			adapter.load(file);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Faild loading file");
		}
	}

}
