/**
 *
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.util.HashSet;
import java.util.Set;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasRGBPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;

/**
 * Extract RGB image from HasPixels adapter.
 *
 * @author Luigi Marini, Devin Bonnie
 * 
 *
 */
public class RGBHistogramExtractor implements Extractor, HasCategory {

    @Override
    public BufferedImageAdapter newAdapter() {
        return new BufferedImageAdapter();
    }

    @Override
    public String getName() {
        return "Pixels to RGB Histogram";
    }

    @Override
    public Descriptor extract(Adapter adapter) throws Exception {

        if (adapter instanceof HasRGBPixels) {

            HasRGBPixels hasPixels = (HasRGBPixels) adapter;
            if (hasPixels.getNumBands() != 3) {
                throw new UnsupportedTypeException(
                        "Expected a color image. Input image is grayscale.");
            }

            RGBHistogramDescriptor rgbHistogram = new RGBHistogramDescriptor();

            int width = hasPixels.getWidth();
            int height = hasPixels.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                        int r = (int) hasPixels.getRGBPixel(y, x, 0);
                        int g = (int) hasPixels.getRGBPixel(y, x, 1);
                        int b = (int) hasPixels.getRGBPixel(y, x, 2);
                        rgbHistogram.add(r, "red");
                        rgbHistogram.add(g, "green");
                        rgbHistogram.add(b, "blue");
                }
            }

            return rgbHistogram;
        } else {
            throw new UnsupportedTypeException();
        }
    }

    @Override
    public Set<Class<? extends Adapter>> supportedAdapters() {
        Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
        adapters.add(BufferedImageAdapter.class);
        return adapters;
    }

    @Override
    public Class<? extends Descriptor> getFeatureType() {
        return RGBHistogramDescriptor.class;
    }

    @Override
    public boolean hasPreview() {
        return true;
    }

    @Override
    public String previewName() {
        return "HistogramVisualizer";
    }

    @Override
    public String getCategory() {
        return "2D";
    }
}
