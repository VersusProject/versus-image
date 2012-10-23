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
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;

/**
 * Extract Grayscale image from HasPixels adapter.
 *
 * @author Luigi Marini, Devin Bonnie
 *
 *
 */
public class GrayscaleHistogramExtractor implements Extractor, HasCategory {

    @Override
    public BufferedImageAdapter newAdapter() {
        return new BufferedImageAdapter();
    }

    @Override
    public String getName() {
        return "Pixels to Grayscale Histogram";
    }

    @Override
    public Descriptor extract(Adapter adapter) throws Exception {


        if (adapter instanceof HasRGBPixels) {

            HasRGBPixels hasPixels = (HasRGBPixels) adapter;

            double[][][] pixels = hasPixels.getRGBPixels();
            int bitsPerPixel = hasPixels.getBitsPerPixel();
            if (bitsPerPixel >= 32) {
                throw new UnsupportedOperationException(
                        "The color depth of the image (" + bitsPerPixel
                        + " bits per pixel) is too big.");
            }

            int numBins = 1 << bitsPerPixel;

            GrayscaleHistogramDescriptor histogram = new GrayscaleHistogramDescriptor(numBins);

            for (int x = 0; x < pixels.length; x++) {
                for (int y = 0; y < pixels[0].length; y++) {

                    int r;
                    if (pixels[x][y].length == 1) {
                        r = (int) pixels[x][y][0];
                        histogram.add(r);
                    } else {
                        throw new UnsupportedTypeException("Expected a grayscale image. Input image is otherwise.");
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
        return GrayscaleHistogramDescriptor.class;
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
