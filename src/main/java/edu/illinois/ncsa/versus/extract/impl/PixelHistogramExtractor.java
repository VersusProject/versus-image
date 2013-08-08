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

			int numBands = hasPixels.getNumBands();
			PixelHistogramDescriptor histogram = new PixelHistogramDescriptor(
					NUM_BINS, numBands);
			int width = hasPixels.getWidth();
			int height = hasPixels.getHeight();
			double scale = (hasPixels.getMaximumPixel()
					- hasPixels.getMinimumPixel())*(1/255.0); 

			if (scale > 0) {
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						for (int band = 0; band < numBands; band++) {
							int bin = (int)  Math.round((hasPixels.getRGBPixel(y, x, band) - hasPixels.getMinimumPixel())
									/ scale);
							if (bin < 0 || bin > NUM_BINS)
								continue;
							histogram.add(bin, band);
						}
					}
				}
			} else {
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						for (int band = 0; band < numBands; band++) {
						histogram.add(0, band);
						}
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
