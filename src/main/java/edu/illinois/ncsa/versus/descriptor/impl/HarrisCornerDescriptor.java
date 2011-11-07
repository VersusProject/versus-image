/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import edu.illinois.ncsa.versus.descriptor.Feature;
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
	

	/**
	 * Takes a double array, converts to Pixels, and stores the raw Pixels in an arraylist (classmember).  
	 * @param input double array containing the corner information (this is given by the openCV 'cvCornerHarris' function. 
	 */
	public HarrisCornerDescriptor(double[][] input) {
	
		rawCorners = new ArrayList<Pixel>();
		
		for( int i=0; i<input.length; i++){
			for( int j=0; j<input[0].length; j++){

				this.rawCorners.add(new Pixel(i,j,input[i][j]));
			}
		}
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
			s = s + "Pixel: x=" + p.x() + " y=" + p.y() + " value=" + p.value()[0];
		}		
		return s;
	}
	
	//##############################################################################################################
	/**
	 * Pixel subclass for the Harris Corner Descriptor. This (general) subclass encapsulates 'pixel information' with its class 
	 * members. This class is public for use in other descriptors if necessary. 
	 *
	 * @author Devin Bonnie
	 *
	 */
	public class Pixel{	
		
		private final int x;
		private final int y;
		private double[] intensity;
		private final String type;
		
		/**
		 * Position Pixel constructor. 
		 * 
		 * @param xPos 
		 * 		X-coordinate (row)
		 * @param yPos 
		 * 		Y-coordinate (column)
		 */
		public Pixel(int xPos, int yPos){
			
			this.x            = xPos;
			this.y            = yPos;
			this.intensity    = null;
			this.type         = "position";
		}
		
		/**
		 * Grayscale Pixel constructor.
		 * 
		 * @param xPos 
		 * 		X-coordinate (row)
		 * @param yPos 
		 * 		Y-coordinate (column)
		 * @param value 
		 * 		Grayscale intensity
		 */
		public Pixel(int xPos, int yPos, double value){
			
			this.intensity    = new double[1];
			this.x            = xPos;
			this.y            = yPos;
			this.intensity[0] = value;
			this.type         = "grayscale";
		}
		
		/**
		 * RGB Pixel constructor.
		 * 
		 * @param xPos 
		 * 		X-coordinate (row)
		 * @param yPos 
		 * 		Y-coordinate (column)
		 * @param value 
		 * 		RGB intensity {double r, double g, double b}
		 */
		public Pixel(int xPos, int yPos, double[] value) throws Exception {
			
			if(value.length != 3){
				throw new Exception("double[] value must have length == 3");
			}
			
			this.intensity = new double[3];
			this.x         = xPos;
			this.y         = yPos;
			this.intensity = value;
			this.type      = "rgb";
		}
		
		/**
		 * Return the X-coordinate
		 * 
		 * @return x 
		 */
		public int x(){
			return x;
		}
		
		/**
		 * Return the y-coordinate
		 * 
		 * @return y 
		 */
		public int y(){
			return y;
		}
		
		/**
		 * Information about the pixel type. 
		 * @return type 
		 * 		String, possible values are "position, grayscale, rgb". 
		 */
		public String type(){
			return type;
		}
		
		/**
		 * Get the pixel's intensity value. 
		 * 
		 * @return intensity 
		 * 		Null if position pixel, double[1] if grayscale, double[3] if rgb. 
		 */
		public double[] value(){
			return intensity;
		}		
	}
	//##############################################################################################################
	
}

