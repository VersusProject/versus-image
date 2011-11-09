/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import java.util.ArrayList;

import edu.illinois.ncsa.versus.descriptor.Feature;
import edu.illinois.ncsa.versus.descriptor.impl.Pixel;

/**
 * Hough Circles
 * 
 * @author Devin Bonnie
 * 
 */
public class HoughCirclesDescriptor implements Feature {

	private ArrayList<Pixel> circles = new ArrayList<Pixel>();
	private final int numCircles;

	public HoughCirclesDescriptor( ArrayList<Pixel> input) {

		this.circles    = input;
		this.numCircles = circles.size();
	}

	public int getNumCircles(){
		return numCircles;
	}
	
	public Pixel getCircle(int index){
		return circles.get(index);
	}
	
	@Override
	public String getType() {
		return this.getClass().toString();
	}
	
	@Override
	public String getName() {
		return "Hough Circles";
	}

	@Override
	public String toString() {
		
		String s = "Showing contents of " + getName() + "\n";

		for( int i=0; i<numCircles; i++){
			Pixel c = circles.get(i);
			s = s + "Circle " + i + ": x= " + c.x() + " y = " + c.y() + " radius= " + c.z() + "\n";
		}		
		return s;
	}
	
}
