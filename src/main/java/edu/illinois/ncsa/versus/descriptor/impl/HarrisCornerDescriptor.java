/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import edu.illinois.ncsa.versus.descriptor.Feature;
import edu.illinois.ncsa.versus.descriptor.impl.Pixel;
import java.util.ArrayList;

/**
 * Harris Corner descriptor: list of pixels from the openCV output. 
 * 
 * @author Devin Bonnie
 * 
 */
public class HarrisCornerDescriptor implements Feature {

	private final ArrayList<Pixel> rawCorners;
	private ArrayList<Pixel> thresholdedCorners;
	private final int numPixels;

	/**
	 * Takes a double array, converts to Pixels, and stores the raw Pixels in an arraylist (classmember).  
	 * @param input double array containing the corner information (this is given by the openCV 'cvCornerHarris' function. 
	 */
	public HarrisCornerDescriptor(double[][] input) {
	
		rawCorners = new ArrayList<Pixel>();
		
		for( int i=0; i<input.length; i++){
			for( int j=0; j<input[0].length; j++){
				
				double[] intensity = {input[i][j]};
				
				this.rawCorners.add(new Pixel(i,j,intensity));
			}
		}
		this.numPixels = rawCorners.size();
	}

	
	public Pixel getCornerPixel(int index){
		return rawCorners.get(index);
	}
	
	/**
	 * Get the total number of pixels.
	 * @return int
	 * 		The number of pixels.
	 */
	public int length(){
		return numPixels;
	}
	
	@Override
	public String getType() {
		return this.getClass().toString();
	}

	/**
	 * Sets the classmember 'thresholdedCorners' given a desired threshold value.
	 * 
	 * @param threshold 
	 * 		Defined (user or set in measure) threshold for corner extraction.
	 * @return thresholdedCorners
	 * 		The Arraylist containing the thresholded pixels. 
	 */
	public ArrayList<Pixel> threshholdCorners(int threshold){
		
		thresholdedCorners = new ArrayList<Pixel>();	
		for(int i=0; i<rawCorners.size(); i++){
			Pixel p = rawCorners.get(i);
			if( p.value()[0] > threshold ){
				thresholdedCorners.add(p);
			}
		}		
		return thresholdedCorners;
	}
	
	@Override
	public String getName() {
		return "Harris Corners";
	}

	@Override
	public String toString() {
		
		String s = "Showing contents of " + getName() + "\n";
		
		thresholdedCorners = new ArrayList<Pixel>();	
		for(int i=0; i<rawCorners.size(); i++){
			Pixel p = rawCorners.get(i);
			s = s + "Pixel: "+i+" x=" + p.x() + " y=" + p.y() + " value=" + p.value()[0];
		}		
		return s;
	}
	
}

