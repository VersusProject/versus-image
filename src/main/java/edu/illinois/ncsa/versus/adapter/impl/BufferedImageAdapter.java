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
    private boolean minCalculated = false;
    private boolean maxCalculated = false;
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_NORMAL;

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

        double[][][] pixels = new double[height][width][numBands];

        for (int band = 0; band < numBands; band++) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    // TODO make sure that getSampleDouble() is the used
                    // correctly
                    try {
                    	double pix = raster.getSampleDouble(col, row, band);
                    	if (pix < min)
                    		min = pix;
                    	if (pix > max)
                    		max = pix;
                        pixels[row][col][band] = pix;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        log.error("Error getting pixels [" + row + ", " + width
                                + ", " + band + "]" + e);
                    }
                }
            }
        }
        setMaxMinCalculated();
        return pixels;
    }

    @Override
    public void load(File file) throws IOException, UnsupportedTypeException {
        image = ImageIO.read(file);
        if (image == null) {
            throw new UnsupportedTypeException("No image reader can decode this file.");
        }
        setMaxMinUncalculated();
    }

    @Override
    public void load(InputStream stream) throws IOException, UnsupportedTypeException {
        try {
            image = ImageIO.read(stream);
            if (image == null) {
                throw new UnsupportedTypeException("No image reader can decode this file.");
            }
        } finally {
            stream.close();
            setMaxMinUncalculated();
        }
    }

    @Override
    public double getRGBPixel(int row, int column, int band) {
        WritableRaster raster = image.getRaster();
        return raster.getSampleDouble(column, row, band);
    }
    
    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getBitsPerPixel() {
        return image.getColorModel().getPixelSize();
    }
    
    @Override
    public int getNumBands() {
        return image.getRaster().getNumBands();
    }

    @Override
    public String getName() {
        return "Buffered Image";
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

    private void setMaxMinUncalculated(){
    	minCalculated = false;
    	maxCalculated = false;
    }

    private void setMaxMinCalculated(){
    	minCalculated = true;
    	maxCalculated = true;
    }
	@Override
	public double getMinimumPixel() {
		if (!minCalculated) {
			double pixels[][][] = getRGBPixels();
			for (int i = 0; i < pixels.length; i++)
				for (int j = 0; j < pixels[0].length; j++)
					for (int k = 0; k < pixels[0][0].length; k++) 
						if (pixels[i][j][k] < min)
							min = pixels[i][j][k];
			minCalculated = true;
		}
		return min;
	}

	@Override
	public double getMaximumPixel() {
		if (!maxCalculated) {
			double pixels[][][] = getRGBPixels();
			for (int i = 0; i < pixels.length; i++)
				for (int j = 0; j < pixels[0].length; j++)
					for (int k = 0; k < pixels[0][0].length; k++) 
						if (pixels[i][j][k] > max)
							max = pixels[i][j][k];
			maxCalculated = true;
		}
		return max;
	}
}
