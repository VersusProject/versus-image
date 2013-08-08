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
	private final int numBands;

	private final int[][] histogram;
	
    
	public PixelHistogramDescriptor() {
		this(256, 3);
	}

	public PixelHistogramDescriptor(int numBins, int numBands) {
		this.numBins   = numBins;
		this.numBands = numBands;
		this.histogram = new int[numBins][numBands];
	}

	public void add(int bin, int band) {
		histogram[bin][band]++;
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
	 * @return the numBands
	 */
	public int getNumBands() {
		return numBands;
	}

	/**
	 * Return the histogram in case the measures need to modify it. 
	 * 
	 * @return The pixel histogram. 
	 */
	public int[][] getHistogram(){
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
			for (int y = 0; y < numBands; y++) {{
					if (histogram[x][y] != 0) {
						s += ("[" + x + ", " + y + "] = "
								+ histogram[x][y] + "\n");
					}
				}
			}
		}
		return s;
	}

	public int get(int bin, int band) {
		return histogram[bin][band];
	}
}
