/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

//import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Binned grayscale histogram.
 * 
 * @author Devin Bonnie
 * 
 */
public class RGBHistogramDescriptor implements Feature {

	private final int[][] rgbHistogram;	
	private final int[][] normalizedRGBHistogram;

	public RGBHistogramDescriptor() {
		this(256);
	}

	public RGBHistogramDescriptor(int numBins) {
		this.rgbHistogram           = new int[numBins][3];
		this.normalizedRGBHistogram = computeNormalizedHistogram(rgbHistogram);
	}

	public void add(int val, String color) throws Exception {
		
		int band = colorInputTest( color );	
		
		rgbHistogram[val][band]++;
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	/**
	 * @return the numBins
	 */
	public int getNumBins() {
		return rgbHistogram.length;
	}

	/**
	 * Normalize the input RGB histogram.
	 * 
	 * @param hist
	 *     Un-normalized histogram (RGB)
	 * @return
	 *     Normalized histogram (RGB)
	 */   
	public int[][] computeNormalizedHistogram( int[][] hist ){
		
		int[][] cdf = new int[hist.length][3];
		
		for( int j=0; j < 3; j++ ){
			
			int min, numPixels;			
			
			//construct the red cdf
			cdf[0][j] = hist[0][j];
			min       =  cdf[0][j];
			numPixels = hist[0][j];
			
			for( int i=1; i < hist.length; i++ ){
				
				cdf[i][j]  = hist[i][j]+cdf[i-1][j];
				numPixels += hist[i][j];
				
				if( min > cdf[i][j] ){
					min = cdf[i][j];
				}			
			}
					
			//normalize the histogram
			for( int i=0; i < cdf.length; i++ ){
			
				cdf[i][0] = Math.round( (float)( ((cdf[i][0]-min)/(numPixels-min))*255 ) );
			}
		
		}
		
		return cdf;
	}
	
	@Override
	public String getName() {
		return "RGB Histogram";
	}

	@Override
	public String toString() {
		
		String s = "Showing contents of " + getName() + "\n";
			
		for (int i = 0; i < 3; i++) {
				
			for (int x = 0; x < rgbHistogram.length; x++) {
				
				if (rgbHistogram[x][i] != 0) {
					s += ("[" + x + ", " + i + "] = "
							+ rgbHistogram[x][i] + "\n");
				}
			}
		}
		
		return s;
	
	}

	public int get(int x, String color) throws Exception {
		
		int band = colorInputTest( color );	
		
		return rgbHistogram[x][band];
	}
	
	public int getNormalized(int x, String color) throws Exception {
		
		int band = colorInputTest( color );
				
		return normalizedRGBHistogram[x][band];
	}
	
	
	private int colorInputTest( String color ) throws Exception {
		
		int band;
		
		if( color.equals("red") ){
			band = 0;
		}
		else if( color.equals("blue") ){
			band = 1;
		}
		else if( color.equals("green") ){
			band = 2;
		}
		else{
			throw new Exception("Invalid color choice. Valid choices are red, green, or blue.");
		}
		
		return band;
	}
}

