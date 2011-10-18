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
public class SurfFeatureDescriptor implements Feature {



	public SurfFeatureDescriptor() {

	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}


	@Override
	public String getName() {
		return "Surf Feature";
	}
	@Override
	public String toString() {
		return "NULL";
	}

}

