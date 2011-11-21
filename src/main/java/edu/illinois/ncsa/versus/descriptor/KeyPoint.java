package edu.illinois.ncsa.versus.descriptor;

/**
 * The KeyPoint class is not strictly a SURF/SIFT keypoint. Rather, it is
 * @author dbonnie
 *
 */
public class KeyPoint {

	private double x;
	private double y;
	private double direction;
	private double laplacian;
	private double hessian;
	private double size;
	private float[] descriptors;
	private int descriptorLength;
	
	public KeyPoint( double[] keypoints, float[] descriptors ){
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