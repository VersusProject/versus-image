package edu.illinois.ncsa.versus.descriptor;

/**
 * Pixel class for the versus image descriptors. This (general) class encapsulates 'pixel information' with its class 
 * members. This class is public for use in other descriptors if necessary. Currently used in Hough Lines, Hough Circles, and Harris Corners.
 *
 * @author Devin Bonnie
 *
 * 
 *
 */
public class Pixel{	
	
	private Integer x;
	private Integer y;
	private Integer z;
	private double[] intensity;
	private String type;
	
	
	/**
	 * 2-D Position Pixel constructor. 
	 * 
	 * @param xPos 
	 * 		X-coordinate (row)
	 * @param yPos 
	 * 		Y-coordinate (column)
	 */
	public Pixel(int xPos, int yPos){
		
		this.x         = new Integer(xPos);
		this.y         = new Integer(yPos);
		this.z         = null;
		this.intensity = null;
		this.type      = "2Dposition";
	}
	
	/**
	 * 3-D Position Pixel constructor. 
	 * 
	 * @param xPos 
	 * 		X-coordinate (row)
	 * @param yPos 
	 * 		Y-coordinate (column)
	 * @param zPos
	 * 		Z-coordinate (depth)
	 */
	public Pixel(int xPos, int yPos, int zPos){
		
		this.x         = new Integer(xPos);
		this.y         = new Integer(yPos);
		this.z         = new Integer(zPos);
		this.intensity = null;
		this.type      = "3Dposition";
	}
	

	/**
	 * 2-D Grayscale/RGB Pixel constructor.
	 * 
	 * @param xPos 
	 * 		X-coordinate (row)
	 * @param yPos 
	 * 		Y-coordinate (column)
	 * @param value
	 * 		RGB intensity {double r, double g, double b} 
	 * 					or
	 * 		grayscale intensity {double val}
	 */
	public Pixel(int xPos, int yPos, double[] value) {
		
		this.intensity = new double[value.length];
		this.x         = new Integer(xPos);
		this.y         = new Integer(yPos);
		this.z         = null;
		this.intensity = value;
		
		if(value.length == 3){
			this.type  = "rgb";
		}
		else if(value.length == 1){
			this.type = "grayscale";
		}
	}
	
	/**
	 * 3-D Grayscale/RGB Pixel constructor.
	 * 
	 * @param xPos 
	 * 		X-coordinate (row)
	 * @param yPos 
	 * 		Y-coordinate (column)
	 * @param zPos 
	 * 		Z-coordinate (depth)
	 * @param value
	 * 		RGB intensity {double r, double g, double b} 
	 * 					or
	 * 		grayscale intensity {double val}
	 */
	public Pixel(int xPos, int yPos, int zPos, double[] value) {
		
		this.intensity = new double[value.length];
		this.x         = new Integer(xPos);
		this.y         = new Integer(yPos);
		this.z         = new Integer(zPos);
		this.intensity = value;
		
		if(value.length == 3){
			this.type  = "rgb";
		}
		else if(value.length == 1){
			this.type = "grayscale";
		}
	}
	
	
	/**
	 * Return the X-coordinate
	 * 
	 * @return x 
	 */
	public int x(){
		return x.intValue();
	}
	
	/**
	 * Return the Y-coordinate
	 * 
	 * @return y 
	 */
	public int y(){
		return y.intValue();
	}
	
	/**
	 * Return the Z-coordinate
	 * 
	 * @return z 
	 */
	public int z(){
		return z.intValue();
	}
	
	/**
	 * Information about the pixel type. 
	 * @return type 
	 * 		Possible values are "position, grayscale, rgb".
	 * 		Can also be user defined. 
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
	
	/**
	 * Set the pixel's row (x).
	 * 
	 * @param xPos 
	 */
	public void setX(int xPos){		
		x = new Integer(xPos);
	}
	
	/**
	 * Set the pixel's column (y).
	 * 
	 * @param yPos 
	 */
	public void setY(int yPos){		
		y = new Integer(yPos);
	}
	
	/**
	 * Set the pixel's depth (z).
	 * 
	 * @param zPos 
	 */
	public void setZ(int zPos){		
		z = new Integer(zPos);
	}
	
	/**
	 * Set the pixel's color value (rgb[3] or grayscale[1]).
	 * 
	 * @param value
	 */
	public void setIntensity(double[] value){
		intensity = new double[value.length];
		intensity = value;
	}
	
	/**
	 * Set the pixel's type.
	 * 
	 * @param name 
	 */
	public void setType(String name){
		type = name;
	}
	
	/**
	 * Return a string containing the relevant information encapsulated in the Pixel.
	 * 
	 * @param s
	 * 		Returned string. 
	 */
	public String toString(){
		String s = new String();
		if( x != null ){
			s = s+"x: "+x.toString()+" ";
		}
		if( y != null ){
			s = s+"y: "+y.toString()+" ";
		}
		if( z != null ){
			s = s+"z: "+z.toString()+" ";
		}
		if( intensity != null){
			s = s+"Intensity: ";
			for( int i=0; i<intensity.length; i++){
				s = s+intensity[i]+" ";
			}
		}
		return s;
	}

}