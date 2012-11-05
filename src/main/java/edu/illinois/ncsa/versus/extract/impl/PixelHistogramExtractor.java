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
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;

/**
 * Extract RGB image from HasPixels adapter.
 *
 * @author Luigi Marini, Devin Bonnie
 *
 *
 */
public class PixelHistogramExtractor implements Extractor, HasCategory {

    @Override
    public BufferedImageAdapter newAdapter() {
        return new BufferedImageAdapter();
    }

    @Override
    public String getName() {
        return "Pixels to Pixel Histogram";
    }

    @Override
    public Descriptor extract(Adapter adapter) throws Exception {
        if (adapter instanceof HasRGBPixels) {
            HasRGBPixels hasPixels = (HasRGBPixels) adapter;
            int bitsPerPixel = hasPixels.getBitsPerPixel();

            if (bitsPerPixel >= 32) {
                throw new UnsupportedOperationException(
                        "The color depth of the image (" + bitsPerPixel
                        + " bits per pixel) is too big.");
            }

            PixelHistogramDescriptor histogram = new PixelHistogramDescriptor(
                    16, 1 << bitsPerPixel);
            int numBands = hasPixels.getNumBands();
            int width = hasPixels.getWidth();
            int height = hasPixels.getHeight();
            if (numBands == 1) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int rgb = (int) hasPixels.getRGBPixel(y, x, 0);
                        histogram.add(rgb, rgb, rgb);
                    }
                }
            } else if (numBands == 2) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int r = (int) hasPixels.getRGBPixel(y, x, 0);
                        int gb = (int) hasPixels.getRGBPixel(y, x, 1);
                        histogram.add(r, gb, gb);
                    }
                }
            } else if (numBands == 3) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int r = (int) hasPixels.getRGBPixel(y, x, 0);
                        int g = (int) hasPixels.getRGBPixel(y, x, 1);
                        int b = (int) hasPixels.getRGBPixel(y, x, 2);
                        histogram.add(r, g, b);
                    }
                }
            }
            return histogram;
        } else {
            throw new UnsupportedTypeException();
        }
    }

    @Override
    public Set<Class<? extends Adapter>> supportedAdapters() {
        Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
        adapters.add(HasRGBPixels.class);
        return adapters;
    }

    @Override
    public Class<? extends Descriptor> getFeatureType() {
        return PixelHistogramDescriptor.class;
    }

    @Override
    public boolean hasPreview() {
        return false;
    }

    @Override
    public String previewName() {
        return null;
    }

    @Override
    public String getCategory() {
        return "2D";
    }
}
