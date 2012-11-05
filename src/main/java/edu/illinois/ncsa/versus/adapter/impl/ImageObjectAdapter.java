/**
 *
 */
package edu.illinois.ncsa.versus.adapter.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.illinois.ncsa.versus.VersusException;
import edu.illinois.ncsa.versus.adapter.FileLoader;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.StreamLoader;
import edu.illinois.ncsa.versus.utility.HasCategory;
import ncsa.im2learn.core.datatype.ImageObject;
import ncsa.im2learn.core.io.ImageLoader;

/**
 * Simple adapter encapsulating Im2Learn ImageObject.
 *
 * @author Luigi Marini
 *
 */
public class ImageObjectAdapter implements HasPixels, FileLoader, StreamLoader, HasCategory {

    /**
     * Im2Learn image object *
     */
    private ImageObject imageObject;

    /**
     * Commons logging *
     */
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

    @Override
    public int getBitsPerPixel() {
        int type = imageObject.getType();
        int numBands = imageObject.getNumBands();
        switch (type) {
            case ImageObject.TYPE_BYTE:
                return 8 * numBands;
            case ImageObject.TYPE_SHORT:
                return 16 * numBands;
            case ImageObject.TYPE_USHORT:
                return 16 * numBands;
            case ImageObject.TYPE_INT:
                return 32 * numBands;
            case ImageObject.TYPE_LONG:
                return 64 * numBands;
            case ImageObject.TYPE_FLOAT:
                return 32 * numBands;
            case ImageObject.TYPE_DOUBLE:
                return 64 * numBands;
            default:
                throw new RuntimeException("Cannot get bits per pixel of image object type " + type);
        }
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
    public void load(InputStream stream) throws IOException, VersusException {
        File file = File.createTempFile("ImageObjectAdapterInput", ".tmp");
        FileOutputStream fos = new FileOutputStream(file);
        try {
            IOUtils.copy(stream, fos);
        } finally {
            fos.close();
            stream.close();
        }
        load(file);
        try {
            file.delete();
        } catch (Exception e) {
            Logger.getLogger(ImageObjectAdapter.class.getName()).log(Level.WARNING, "Cannot delete temp file " + file, e);
        }
    }

    @Override
    public double getRGBPixel(int row, int column, int band) {
        return imageObject.getDouble(row, column, band);
    }

    @Override
    public int getWidth() {
        return imageObject.getNumCols();
    }

    @Override
    public int getHeight() {
        return imageObject.getNumRows();
    }
    
    @Override
    public int getNumBands() {
        return imageObject.getNumBands();
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

    @Override
    public String getCategory() {
        return "2D";
    }
}
