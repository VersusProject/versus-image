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

/**
 * Extract RGB image from HasPixels adapter.
 *
 * @author Luigi Marini, Devin Bonnie
 *
 *
 */
public class PixelHistogramExtractor implements Extractor {

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
            double[][][] pixels = hasPixels.getRGBPixels();
            int bitsPerPixel = hasPixels.getBitsPerPixel();

            if (bitsPerPixel >= 32) {
                throw new UnsupportedOperationException(
                        "The color depth of the image (" + bitsPerPixel
                        + " bits per pixel) is too big.");
            }

            PixelHistogramDescriptor histogram = new PixelHistogramDescriptor(
                    16, 1 << bitsPerPixel);

            for (int x = 0; x < pixels.length; x++) {
                for (int y = 0; y < pixels[0].length; y++) {
                    int length = pixels[x][y].length;
                    if (length == 1) {
                        int rgb = (int) pixels[x][y][0];
                        histogram.add(rgb, rgb, rgb);
                    } else if (length == 2) {
                        int r = (int) pixels[x][y][0];
                        int gb = (int) pixels[x][y][1];
                        histogram.add(r, gb, gb);
                    } else if (length >= 3) {
                        int r = (int) pixels[x][y][0];
                        int g = (int) pixels[x][y][1];
                        int b = (int) pixels[x][y][2];
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
}
