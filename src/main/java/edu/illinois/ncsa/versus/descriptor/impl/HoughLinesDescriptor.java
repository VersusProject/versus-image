/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import java.util.ArrayList;

import edu.illinois.ncsa.versus.descriptor.Feature;
import edu.illinois.ncsa.versus.descriptor.impl.Pixel;

/**
 * Hough Lines from openCV. Contains the lines processed from an image in an Arraylist as a Pixel array. 
 * 
 * @author Devin Bonnie
 * 
 */
public class HoughLinesDescriptor implements Feature {

	private ArrayList<Pixel[]> lines;
	private final int numLines;

	public HoughLinesDescriptor(ArrayList<Pixel[]> input) {

		this.lines    = input;
		this.numLines = lines.size();
	}

	/**
	 * Get the line at the specified index. 
	 * 
	 * @param index
	 * 		integer specifying the index. 
	 * @return
	 * 		Pixel Array containing the line information
	 */
	public Pixel[] getLine(int index){
		return lines.get(index);
	}
	
	/**
	 * Get the total number of lines. 
	 * 
	 * @return
	 * 		integer representing the total number of lines found. 
	 */
	public int getNumLines(){
		return numLines;
	}
		
	@Override
	public String getType() {
		return this.getClass().toString();
	}
	
	@Override
	public String getName() {
		return "Hough Lines";
	}

	@Override
	public String toString() {
		String s = "Showing contents of " + getName() + "\n";
		
		for( int i=0; i<numLines; i++){
			Pixel[] ll = lines.get(i);
			s = s + "Line " + i + ": x1= " + ll[0].x() + " y1 = " + ll[0].y() + "-- x2= " + ll[1].x() + " y2 = " + ll[1].y() + "\n";
		}		
		return s;
	}
	
}
