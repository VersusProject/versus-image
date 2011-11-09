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

	private final ArrayList<SurfPoint> surfFeatures;
	private final int numPoints;
	

	public SurfFeatureDescriptor( ArrayList<SurfPoint> input) {
		this.surfFeatures = input;
		this.numPoints    = input.size(); 
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	@Override
	public String getName() {
		return "Surf Features";
	}

	@Override
	public String toString() {
		
		String s = "Showing contents of " + getName() + "\n";

		for(int i=0; i<numPoints; i++){
			
			SurfPoint pt =  surfFeatures.get(i);
			
			s = s + "Keypoint " + i + "x: " +pt.x()+ "y: "+pt.y()+ "direction: "+pt.direction()
					+"laplacian: "+pt.laplacian()+"hessian: "+pt.hessian()+ "size: "+pt.size(); 
			s = s + " Descriptor: ";
						
			for(int j=0; j<pt.getDescriptorLength(); j++){
				s = s + pt.getDescriptorValue(i) + " ";
			}			
		}
		
		return s;	
	}
	
	/**
	 * Return the number of SURF keypoint / descriptor pairs.
	 * 
	 * @return int
	 */
	public int getLength(){
		return numPoints;
	}
	
	/**
	 * Returns the relevant point information.
	 *
	 * @return SurfPoint
	 */
	public SurfPoint get(int index) throws Exception{
		
		if(index >= numPoints){
			throw new Exception("Not a valid keypoint index: out of bounds");
		}
		
		return surfFeatures.get(index);
	}
	
	//#############################################################################
	
	public static class SurfPoint {

		private double x;
		private double y;
		private double direction;
		private double laplacian;
		private double hessian;
		private double size;
		private float[] descriptors;
		private int descriptorLength;
		
		public SurfPoint( double[] keypoints, float[] descriptors ){
			this.x                = keypoints[0];
			this.y                = keypoints[1];
			this.direction        = keypoints[2];
			this.laplacian        = keypoints[3];
			this.hessian          = keypoints[4];
			this.size             = keypoints[5];
			this.descriptors      = descriptors;			
			this.descriptorLength = descriptors.length;
		}
		
		
		public double x(){
			return x;
		}
		
		public double y(){
			return y;
		}
		public double direction(){
			return direction;
		}
		public double laplacian(){
			return laplacian;
		}
		public double hessian(){
			return hessian;
		}
		public double size(){
			return size;
		}
		public int getDescriptorLength(){
			return descriptorLength;
		}
		public float[] getDescriptorArray(){
			return descriptors;
		}
		public float getDescriptorValue(int index){
			return descriptors[index];
		}
		
	}
	
	
}

