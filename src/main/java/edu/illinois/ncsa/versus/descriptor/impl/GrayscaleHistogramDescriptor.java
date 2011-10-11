/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Binned grayscale histogram.
 * 
 * @author Devin Bonnie
 * 
 */
public class GrayscaleHistogramDescriptor implements Feature {

	private final int[] histogram;	
	private int[] normalizedHistogram;

	public GrayscaleHistogramDescriptor() {
		this(256);
	}

	public GrayscaleHistogramDescriptor(int numBins) {
		this.histogram           = new int[numBins];
		this.normalizedHistogram = new int[numBins];
	}

	public void add(int val) {
		histogram[val]++;
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	/**
	 * @return the grayscale histogram
	 */
	public int[] getHistogram(){
		return histogram; 
	}
	
	/**
	 * @return the numBins
	 */
	public int getNumBins() {
		return histogram.length;
	}

	/**
	 * Normalize the input grayscale histogram.
	 * 
	 * @param hist
	 *     Un-normalized histogram (grayscale)
	 * @return
	 *     Normalized histogram (grayscale)
	 */   
	public int[] computeNormalizedHistogram( ){
		
		int min, numPixels;
		int[] cdf = new int[histogram.length];
		
		//construct the cdf
		cdf[0]    = histogram[0];
		min       =  cdf[0];
		numPixels = histogram[0];
		
		for( int i=1; i < histogram.length; i++ ){
			
			cdf[i]     = histogram[i]+cdf[i-1];
			numPixels += histogram[i];
			
			if( min > cdf[i] ){
				min = cdf[i];
			}			
		}				
		//normalize the histogram
		for( int i=0; i < cdf.length; i++ ){
		
			cdf[i] = Math.round( (float)( ((float)(cdf[i]-min)/(float)(numPixels-min))*255 ) );
		}		
		return cdf;
	}
	
	@Override
	public String getName() {
		return "Grayscale Histogram";
	}

	@Override
	public String toString() {
		
		String s = "Showing contents of " + getName() + "\n";
		
		for (int x = 0; x < histogram.length; x++) {

			if (histogram[x] != 0) {
				s += ("[" + x + "] = "
						+ histogram[x] + "\n");
			}
		}
		return s;
	}

	public int get(int x){
		return histogram[x];
	}
	
	public int getNormalized(int x){
		return normalizedHistogram[x];
	}
}
