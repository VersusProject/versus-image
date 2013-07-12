/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Binned pixel histogram.
 * 
 * @author Luigi Marini, Devin Bonnie
 * 
 */
public class PixelHistogramDescriptor implements Feature {

	private final int numBins;

	private final int[][][] histogram;
	
    
	public PixelHistogramDescriptor() {
		this(256);
	}

	public PixelHistogramDescriptor(int numBins) {
		this.numBins   = numBins;
		this.histogram = new int[numBins][numBins][numBins];
	}

	public void add(int r, int g, int b) {
		histogram[r][g][b]++;
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

	/**
	 * Return the histogram in case the measures need to modify it. 
	 * 
	 * @return The pixel histogram. 
	 */
	public int[][][] getHistogram(){
		return histogram; 
	}
	

	@Override
	public String getName() {
		return "Pixel Histogram";
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
