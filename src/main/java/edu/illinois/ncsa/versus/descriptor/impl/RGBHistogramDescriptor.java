/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Binned color histogram.
 * 
 * @author Luigi Marini
 * 
 */
public class RGBHistogramDescriptor implements Feature {

	private final int numBins;

	private final int[][][] histogram;

	public RGBHistogramDescriptor() {
		this(16);
	}

	public RGBHistogramDescriptor(int numBins) {
		this.numBins = numBins;
		this.histogram = new int[numBins][numBins][numBins];
	}

	public void add(int r, int g, int b) {
		int intervalSize = 256 / numBins;
		int x = r / intervalSize;
		int y = g / intervalSize;
		int z = b / intervalSize;
		histogram[x][y][z]++;
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	/**
	 * @return the numBins
	 */
	public int getNumBins() {
		return numBins;
	}

	@Override
	public String getName() {
		return "Normalized Histogram";
	}

	@Override
	public String toString() {
		String s = "Showing contents of " + getName() + "\n";
		for (int x = 0; x < numBins; x++) {
			for (int y = 0; y < numBins; y++) {
				for (int z = 0; z < numBins; z++) {
					if (histogram[x][y][z] != 0) {
						s += ("[" + x + ", " + y + ", " + z + "] = "
								+ histogram[x][y][z] + "\n");
					}
				}
			}
		}
		return s;
	}

	public int get(int x, int y, int z) {
		return histogram[x][y][z];
	}
}
