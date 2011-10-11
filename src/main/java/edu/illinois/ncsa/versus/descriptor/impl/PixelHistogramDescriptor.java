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
	
	//private final int[][][] normalizedHistogram;

	public PixelHistogramDescriptor() {
		this(16);
		//TODO: should be changed to 256, the measures can deal with binning issues. 
	}

	public PixelHistogramDescriptor(int numBins) {
		this.numBins   = numBins;
		this.histogram = new int[numBins][numBins][numBins];
		
		//this.normalizedHistogram = getNormalizedHistogram( this.histogram );
		
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
