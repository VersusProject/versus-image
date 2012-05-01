/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

//import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Binned RGB histogram.
 * 
 * @author Devin Bonnie
 * 
 */
public class RGBHistogramDescriptor implements Feature {

	private final int[][] rgbHistogram;	

	public RGBHistogramDescriptor() {
		this(256);
	}

	public RGBHistogramDescriptor(int numBins) {
		this.rgbHistogram = new int[numBins][3];
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
	 * @return int[][]
	 * 		The rgb histogram. 
	 */
	public int[][] getHistogram(){
		return rgbHistogram;
	}
	
	/**
	 * @return the number of bins
	 */
	public int getNumBins() {
		return rgbHistogram.length;
	}
	
	/**
	 * @return the number of bands
	 */
	public int getNumBands() {
		return rgbHistogram[0].length;
	}

	/**
	 * Normalize the input RGB histogram.
	 * 
	 * @return int[][]
	 *     Normalized histogram (RGB)
	 */   
	public int[][] computeNormalizedHistogram(){
		
		int[][] cdf = new int[rgbHistogram.length][3];
		
		for( int j=0; j < 3; j++ ){
			
			int min, numPixels;			
			
			cdf[0][j] = rgbHistogram[0][j];
			min       =  cdf[0][j];
			numPixels = rgbHistogram[0][j];
			
			for( int i=1; i < rgbHistogram.length; i++ ){
				
				cdf[i][j]  = rgbHistogram[i][j]+cdf[i-1][j];
				numPixels += rgbHistogram[i][j];
				
				if( min > cdf[i][j] ){
					min = cdf[i][j];
				}			
			}
					
			//normalize the histogram
			for( int i=0; i < cdf.length; i++ ){
			
				cdf[i][j] = Math.round( (float)( (float)((cdf[i][j]-min)/(float)(numPixels-min))*255 ) );
			}		
		}		
		return cdf;
	}
	
	@Override
	public String getName() {
		return "RGBHistogramDescriptor";
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

	public int get(int x, int band) {
		return rgbHistogram[x][band];
	}
	
	public int get(int x, String color) throws Exception {
		
		int band = colorInputTest( color );			
		return rgbHistogram[x][band];
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

