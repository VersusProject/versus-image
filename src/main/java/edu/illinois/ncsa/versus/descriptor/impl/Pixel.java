
package edu.illinois.ncsa.versus.descriptor.impl;

/**
 * Pixel class for the versus image descriptors. This (general) class encapsulates 'pixel information' with its class 
 * members. This class is public for use in other descriptors if necessary. Currently used in HoughLines and Harris Corners.
 *
 * @author Devin Bonnie
 *
 */
public class Pixel{	
	
	private int x;
	private int y;
	//private int depth; 
	private double[] intensity;
	private String type;
	
	/**
	 * Position Pixel constructor. 
	 * 
	 * @param int 
	 * 		X-coordinate (row)
	 * @param int 
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
	 * @param int 
	 * 		X-coordinate (row)
	 * @param int 
	 * 		Y-coordinate (column)
	 * @param double 
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
	 * @param int 
	 * 		X-coordinate (row)
	 * @param int 
	 * 		Y-coordinate (column)
	 * @param double[3] 
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
	 * @return int 
	 */
	public int x(){
		return x;
	}
	
	/**
	 * Return the y-coordinate
	 * 
	 * @return int 
	 */
	public int y(){
		return y;
	}
	
	/**
	 * Information about the pixel type. 
	 * @return string 
	 * 		String, possible values are "position, grayscale, rgb". 
	 */
	public String type(){
		return type;
	}
	
	/**
	 * Get the pixel's intensity value. 
	 * 
	 * @return double[] 
	 * 		Null if position pixel, double[1] if grayscale, double[3] if rgb. 
	 */
	public double[] value(){
		return intensity;
	}	
	
	
	public void set(int xPos, int yPos){
		
		x            = xPos;
		y            = yPos;
		intensity    = null;
		type         = "position";
	}
	
	public void set(int xPos, int yPos, double value){
		
		intensity = new double[1];

		x            = xPos;
		y            = yPos;
		intensity[0] = value;
		type         = "grayscale";
	}
	
	public void set(int xPos, int yPos, double[] value){
		
		intensity = new double[3];
		
		x            = xPos;
		y            = yPos;
		intensity    = value;
		type         = "rgb";
	}

}