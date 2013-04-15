/**
 * 
 */
package edu.illinois.ncsa.versus.adapter.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ncsa.im2learn.core.datatype.ImageObject;
import ncsa.im2learn.core.io.ImageLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.illinois.ncsa.versus.adapter.FileLoader;
import edu.illinois.ncsa.versus.adapter.HasPixels;

/**
 * Simple adapter encapsulating Im2Learn ImageObject.
 * 
 * @author Luigi Marini
 * 
 */
public class ImageObjectAdapter implements HasPixels, FileLoader {

	/** Im2Learn image object **/
	private ImageObject imageObject;

	/** Commons logging **/
	private static Log log = LogFactory.getLog(ImageObjectAdapter.class);

	/**
	 * Wrap an Im2Learn ImageObject.
	 * 
	 * @param imageObject
	 */
	public ImageObjectAdapter(ImageObject imageObject) {
		this.imageObject = imageObject;
	}

	/**
	 * Create an empty instance of the adapter.
	 */
	public ImageObjectAdapter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.illinois.ncsa.compare.extract.PixelExtractor#getPixels()
	 */
	@Override
	public double[][][] getRGBPixels() {

		int numBands = imageObject.getNumBands();
		int numRows = imageObject.getNumRows();
		int numCols = imageObject.getNumCols();

		double[][][] pixels = new double[numRows][numCols][numBands];

		for (int band = 0; band < numBands; band++) {
			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < numCols; col++) {
					pixels[row][col][band] = imageObject.getDouble(row, col,
							band);
				}
			}
		}
		return pixels;
	}

	/**
	 * Get Im2learn image object.
	 * 
	 * @return image object
	 */
	public ImageObject getImageObject() {
		return imageObject;
	}

	@Override
	public void load(File file) throws IOException {
		imageObject = ImageLoader.readImage(file.getAbsolutePath());
	}

	@Override
	public double getRGBPixel(int row, int column, int band) {
		return imageObject.getDouble(row, column, band);
	}

	@Override
	public String getName() {
		return "Image Object";
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
}
