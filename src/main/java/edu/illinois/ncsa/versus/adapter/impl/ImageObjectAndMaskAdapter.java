/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package edu.illinois.ncsa.versus.adapter.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import edu.illinois.ncsa.versus.VersusException;
import edu.illinois.ncsa.versus.adapter.HasMask;
import ncsa.im2learn.core.datatype.ImageObject;
import ncsa.im2learn.core.io.ImageLoader;

/**
 *
 * @author antoinev
 */
public class ImageObjectAndMaskAdapter extends ImageObjectAdapter
        implements HasMask {

    private ImageObject mask;

    public ImageObjectAndMaskAdapter() {
    }
    
    public ImageObjectAndMaskAdapter(ImageObject image, ImageObject mask) {
        super(image);
        this.mask = mask;
    }
    
    @Override
    public String getName() {
        return "Image object and mask adapter";
    }

    @Override
    public List<String> getSupportedMediaTypes() {
        return Arrays.asList("image/*");
    }

    @Override
    public String getCategory() {
        return "2D";
    }

    public ImageObject getMaskImageObject() {
        return mask;
    }
    
    
    @Override
    public double[][] getMask() {
        int numRows = mask.getNumRows();
        int numCols = mask.getNumCols();

        double[][] pixels = new double[numRows][numCols];

        // Check wether a null mask should be all 0 or all 1
        if (mask != null) {
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    pixels[row][col] = mask.getDouble(row, col, 0);
                }
            }
        }
        return pixels;
    }

    @Override
    public double getMaskPixel(int row, int column) {
        return mask.getDouble(row, column, 0);
    }

    public void load(File imageFile, File maskFile) throws IOException, VersusException {
        super.load(imageFile);
        loadMask(maskFile);
    }

    public void load(InputStream imageStream, InputStream maskStream)
            throws IOException, VersusException {
        super.load(imageStream);
        File file = File.createTempFile("ImageObjectAdapterInput", ".tmp");
        FileOutputStream fos = new FileOutputStream(file);
        try {
            IOUtils.copy(maskStream, fos);
        } finally {
            fos.close();
            maskStream.close();
        }
        loadMask(file);
        try {
            file.delete();
        } catch (Exception e) {
            Logger.getLogger(ImageObjectAdapter.class.getName()).log(Level.WARNING, "Cannot delete temp file " + file, e);
        }
    }

    private void loadMask(File maskFile) throws IOException, VersusException {
        mask = ImageLoader.readImage(maskFile.getAbsolutePath());
        if (getWidth() != mask.getNumCols()
                || getHeight() != mask.getNumRows()) {
            throw new VersusException("The mask must have the same dimensions"
                    + " than the image.");
        }
        if (mask.getNumBands() != 1) {
            throw new VersusException("The mask must have one band.");
        }
    }
}
