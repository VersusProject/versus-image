/**
 *
 */
package edu.illinois.ncsa.versus.adapter.impl;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.FileLoader;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.StreamLoader;
import edu.illinois.ncsa.versus.utility.HasCategory;
import javax.imageio.ImageIO;

/**
 * Adapter for java buffered images.
 *
 * @author Luigi Marini
 *
 */
public class BufferedImageAdapter implements HasPixels, FileLoader, StreamLoader, HasCategory {

    /**
     * Buffered image *
     */
    private BufferedImage image;

    /**
     * Commons logging *
     */
    private static Log log = LogFactory.getLog(BufferedImageAdapter.class);

    /**
     * Empty adapter.
     */
    public BufferedImageAdapter() {
    }

    /**
     * Adapter for java buffered images.
     *
     * @param image
     */
    public BufferedImageAdapter(BufferedImage image) {
        this.image = image;
    }

    /**
     * Get buffered image.
     *
     * @return buffered image
     */
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public double[][][] getRGBPixels() {
        WritableRaster raster = image.getRaster();
        int numBands = raster.getNumBands();
        int width = raster.getWidth();
        int height = raster.getHeight();

//		log.trace("Height: " + height + ", Width: " + width + ", Bands: "
//				+ numBands);

        double[][][] pixels = new double[height][width][numBands];

        for (int band = 0; band < numBands; band++) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    // TODO make sure that getSampleDouble() is the used
                    // correctly
                    try {
                        pixels[row][col][band] = raster.getSampleDouble(col,
                                row, band);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        log.error("Error getting pixels [" + row + ", " + width
                                + ", " + band + "]" + e);
                    }
                }
            }
        }
        return pixels;
    }

    @Override
    public void load(File file) throws IOException, UnsupportedTypeException {
        image = ImageIO.read(file);
        if(image == null) {
            throw new UnsupportedTypeException("No image reader can decode this file.");
        }
    }

    @Override
    public void load(InputStream stream) throws IOException, UnsupportedTypeException {
        image = ImageIO.read(stream);
        if(image == null) {
            throw new UnsupportedTypeException("No image reader can decode this file.");
        }
    }

    @Override
    public double getRGBPixel(int row, int column, int band) {
        WritableRaster raster = image.getRaster();
        return raster.getSampleDouble(column, row, band);
    }

    @Override
    public int getBitsPerPixel() {
        return image.getColorModel().getPixelSize();
    }

    @Override
    public String getName() {
        return "Buffered Image";
    }

    @Override
    public double getHSVPixel(int row, int column, int band) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double[][][] getHSVPixels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getSupportedMediaTypes() {
        List<String> mediaTypes = new ArrayList<String>();
        mediaTypes.add("image/*");
        return mediaTypes;
    }

    @Override
    public String getCategory() {
        return "2D";
    }
}
