/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.util.HashSet;
import java.util.Set;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Extract RGB image from HasPixels adapter.
 * 
 * @author Luigi Marini
 * 
 */
public class RGBHistogramExtractor implements Extractor {

	@Override
	public BufferedImageAdapter newAdapter() {
		return new BufferedImageAdapter();
	}

	@Override
	public String getName() {
		return "Pixels to Histogram";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws Exception {
		if (adapter instanceof HasPixels) {
			HasPixels hasPixels = (HasPixels) adapter;
			double[][][] pixels = hasPixels.getRGBPixels();
			RGBHistogramDescriptor histogram = new RGBHistogramDescriptor();

			for (int x = 0; x < pixels.length; x++) {
				for (int y = 0; y < pixels[0].length; y++) {
					int r, g, b;
					if (pixels[x][y].length == 1) {
						r = (int) pixels[x][y][0];
						g = (int) pixels[x][y][0];
						b = (int) pixels[x][y][0];
						histogram.add(r,g,b);
					}
					if (pixels[x][y].length == 2) {
						r = (int) pixels[x][y][0];
						g = (int) pixels[x][y][1];
						b = (int) pixels[x][y][1];
						histogram.add(r,g,b);
					}
					if (pixels[x][y].length >= 3) {
						r = (int) pixels[x][y][0];
						g = (int) pixels[x][y][1];
						b = (int) pixels[x][y][2];
						histogram.add(r,g,b);
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
		adapters.add(HasPixels.class);
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType() {
		return RGBHistogramDescriptor.class;
	}
}
