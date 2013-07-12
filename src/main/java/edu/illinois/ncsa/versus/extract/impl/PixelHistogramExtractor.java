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

	
	private static final int NUM_BINS = 256;
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

			PixelHistogramDescriptor histogram = new PixelHistogramDescriptor(
					NUM_BINS);
			int numBands = hasPixels.getNumBands();
			int width = hasPixels.getWidth();
			int height = hasPixels.getHeight();
			double scale = hasPixels.getMaximumPixel()
					- hasPixels.getMinimumPixel();

			if (scale > 0) {
				if (numBands == 1) {
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							int rgb = (int) Math.round((hasPixels.getRGBPixel(
									y, x, 0) - hasPixels.getMinimumPixel())
									/ scale);
							histogram.add(rgb, rgb, rgb);
						}
					}
				} else if (numBands == 2) {
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							int r = (int)  Math.round((hasPixels.getRGBPixel(y, x, 0) - hasPixels.getMinimumPixel())
									/ scale);
							int gb = (int)  Math.round((hasPixels.getRGBPixel(y, x, 1) - hasPixels.getMinimumPixel())
									/ scale);
							histogram.add(r, gb, gb);
						}
					}
				} else if (numBands == 3) {
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							int r = (int)  Math.round((hasPixels.getRGBPixel(y, x, 0) - hasPixels.getMinimumPixel())
									/ scale);
							int g = (int)  Math.round((hasPixels.getRGBPixel(y, x, 1) - hasPixels.getMinimumPixel())
									/ scale);
							int b = (int)  Math.round((hasPixels.getRGBPixel(y, x, 2) - hasPixels.getMinimumPixel())
									/ scale);
							histogram.add(r, g, b);
						}
					}
				}
			} else {
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						histogram.add(0, 0, 0);
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
