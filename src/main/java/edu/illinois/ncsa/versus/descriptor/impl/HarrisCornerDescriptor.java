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
public class HarrisCornerDescriptor implements Feature {

	private final double[][] cornerImage;
	

	public HarrisCornerDescriptor(double[][] input) {
	
		this.cornerImage = input;
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	@Override
	public String getName() {
		return "Harris Corners";
	}

	@Override
	public String toString() {
		String s = "Showing contents of " + getName() + "\n";

		return s;
	}
}

