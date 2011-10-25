/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import java.util.ArrayList;
import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Surf features extracted from OpenCV (via JavaCV).
 * 
 * A keypoint is given as double[] = {x coordinate, y coordinate, direction, laplacian, hessian, size}
 *
 * A descriptor is given as float[64] or [128] (default is 64)
 * 
 * @author Devin Bonnie
 * 
 */
public class SurfFeatureDescriptor implements Feature {

	private final ArrayList<double[]> cvKeypoints;
	private final ArrayList<float[]>  cvDescriptors;
	private final int                 numKeypoints;

	public SurfFeatureDescriptor( ArrayList<double[]> keys, ArrayList<float[]> desc ) {
		this.cvKeypoints   = keys;
		this.cvDescriptors = desc;
		this.numKeypoints  = cvKeypoints.size(); //is also equal to cvDescriptors.size()
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
		
		String s = "Showing contents of " + getName() + "\n";
		//TODO		
		return s;	
	}
	
	public int getLength(){
		return numKeypoints;
	}
	
	/*
	 * Returns the relevent keypoint information.
	 * @return info[]
	 * 		double array containing {x,y,direction,laplacian,hessian,size}
	 *      http://opencv.willowgarage.com/documentation/python/feature_detection.html
	 */
	public double[] getKeypoint(int index) throws Exception{
		
		if(index >= numKeypoints){
			throw new Exception("Not a valid keypoint index: out of bounds");
		}
		
		return cvKeypoints.get(index);
	}
	
	/*
	 * Returns the relevent descriptor information.
	 * @return info[]
	 * 		float array containing the descriptor info (default is 64 entries). 
	 */
	public float[] getDescriptor(int index) throws Exception {
		
		if(index >= numKeypoints){
			throw new Exception("Not a valid keypoint index: out of bounds");
		}

		return cvDescriptors.get(index);
	}
	
	public int getKeypointLength(){
		return cvKeypoints.get(0).length;
	}
	public int getDescriptorLength(){
		return cvDescriptors.get(0).length;
	}
	
}

