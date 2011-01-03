package edu.illinois.ncsa.versus.adapter;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;

public class BufferedImageAdapterTest {

	@Test
	public void testBufferedImageAdapterBufferedImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPixels() {

		BufferedImageAdapter adapter = new BufferedImageAdapter();

		try {
			adapter.load(new File("data/test_1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Faild loading file");
		}

		double[][][] pixels = adapter.getRGBPixels();

		if (pixels.length < 1 || pixels[0].length < 1
				|| pixels[0][0].length < 1) {
			fail("Loaded image has missing pixels");
		}
	}

	@Test
	public void testLoad() {

		BufferedImageAdapter adapter = new BufferedImageAdapter();

		File file = new File("data/test_1.jpg");

		System.out.print("Absolute path: " + file.getAbsolutePath());

		try {
			adapter.load(file);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Faild loading file");
		}
	}

}
