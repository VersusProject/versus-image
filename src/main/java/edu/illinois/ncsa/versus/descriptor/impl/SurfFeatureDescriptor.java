/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import java.util.ArrayList;
import edu.illinois.ncsa.versus.descriptor.Feature;

/**
 * Surf features extracted from OpenCV (via JavaCV).
 * 
 * The keypoints and descriptors are each stored in an ArrayList (container). 
 * 
 * A keypoint is given as double[] = {x coordinate, y coordinate, direction, laplacian, hessian, size}
 * A descriptor is given as float[64] or [128] (default from extractor is 64)
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

		for(int i=0; i<numKeypoints; i++){
			
			double[] keyInfo =  cvKeypoints.get(i);
			float[] descInfo =  cvDescriptors.get(i);
			
			s = s + "Keypoint " + i + "x: " +keyInfo[0]+ "y: "+keyInfo[1]+ "direction: "+keyInfo[2]
					+"laplacian: "+keyInfo[3]+"hessian: "+keyInfo[4]+ "size: "+keyInfo[5]; 
			s = s + "Descriptor: ";
			for(int j=0; j<descInfo.length; j++){
				s = s + descInfo + " ";
			}			
		}
		
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
	
	/*
	 * Returns the length of a keypoint container.
	 * @return int length
	 */
	public int getKeypointLength(){
		return cvKeypoints.get(0).length;
	}
	
	/*
	 * Returns the length of a descriptor container.
	 * @return int length
	 */
	public int getDescriptorLength(){
		return cvDescriptors.get(0).length;
	}
	
}

