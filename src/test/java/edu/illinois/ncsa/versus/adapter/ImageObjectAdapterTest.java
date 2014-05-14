package edu.illinois.ncsa.versus.adapter;

import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

public class ImageObjectAdapterTest {

    @Test
    public void testGetPixels() {

        ImageObjectAdapter adapter = new ImageObjectAdapter();

        try {
            adapter.load(new File("data/test_2.jpg"));
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

        File file = new File("data/test_1.jpg");

        System.out.print("Absolute path: " + file.getAbsolutePath());

        try {
            adapter.load(file);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Faild loading file");
        }
    }

    @Test
    public void testLoadRGB8BitPng() throws IOException {
        loadImage("data/color8bit.png", 24);
    }
    
    @Test
    public void testLoadRGB24BitPng() throws IOException {
        loadImage("data/color24bit.png", 24);
    }

    @Ignore("No loader for grayscale jpg on linux.")
    @Test
    public void testLoadGrayscale8BitJpg() throws IOException {
        loadImage("data/grayscale8bit.jpg", 8);
    }

    @Test
    public void testLoadGrayscale8BitPng() throws IOException {
        loadImage("data/grayscale8bit.png", 8);
    }

    @Test
    public void testLoadGrayscale8BitTif() throws IOException {
        loadImage("data/grayscale8bit.tif", 8);
    }

    @Test
    public void testLoadGrayscale16BitPng() throws IOException {
        loadImage("data/grayscale16bit.png", 16);
    }

    @Test
    public void testLoadGrayscale16BitTif() throws IOException {
        loadImage("data/grayscale16bit.tif", 16);
    }

    @Test
    public void testLoadGrayscale32BitTif() throws IOException {
        loadImage("data/grayscale32bit.tif", 32);
    }
    
    private void loadImage(String image, int bitPerPixel) throws IOException {
        ImageObjectAdapter adapter = new ImageObjectAdapter();
        adapter.load(new File(image));
        assertEquals(bitPerPixel, adapter.getBitsPerPixel());
    }
}
