package edu.illinois.ncsa.versus.descriptor;

import java.lang.Double;
/**
 * The KeyPoint class is not strictly a SURF/SIFT keypoint. Rather, it is general container holding the keypoint info as well as
 * descriptor information
 * 
 * @author Devin Bonnie
 *
 */
public class KeyPoint {

	private Double x;
	private Double y;
	private Double direction;
	private Double laplacian;
	private Double hessian;
	private Double size;
	private Double scale;
	private float[] descriptors;
	private int descriptorLength;
	
	/**
	 * Constructor given OpenCV SURF information. 
	 * 
	 * @param keypoints
	 * @param descriptors
	 */
	public KeyPoint( double[] keypoints, float[] descriptors ){
		this.x                = new Double(keypoints[0]);
		this.y                = new Double(keypoints[1]);
		this.direction        = new Double(keypoints[2]);
		this.laplacian        = new Double(keypoints[3]);
		this.hessian          = new Double(keypoints[4]);
		this.size             = new Double(keypoints[5]);
		this.descriptors      = descriptors;			
		this.descriptorLength = descriptors.length;
		//
		this.scale            = null;
	}
	
	/**
	 * Constructor given ImageJ (SIFT / MOPS features)
	 * 
	 * @param location
	 * @param orientation
	 * @param scale 
	 * @param descriptors
	 * 
	 * @return
	 */
	public KeyPoint( float[] location, float orientation, float scale, float[] descriptors ){
		this.x                = new Double(location[0]);
		this.y                = new Double(location[1]);
		this.direction        = new Double(orientation);
		this.scale            = new Double(scale);
		this.descriptors      = descriptors;			
		this.descriptorLength = descriptors.length;
		//
		this.laplacian        = null;
		this.hessian          = null;
		this.size             = null;
	}
	
	public double x(){
		return x.doubleValue();
	}		
	public double y(){
		return y.doubleValue();
	}
	public double direction(){
		return direction.doubleValue();
	}
	public double laplacian(){
		return laplacian.doubleValue();
	}
	public double hessian(){
		return hessian.doubleValue();
	}
	public double size(){
		return size.doubleValue();
	}
	public double scale(){
		return scale.doubleValue();
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
	
	public String toString(){
		
		String s = new String();
		
		if( x != null){
			s = s+"x= "+x.toString();
		}
		if( y != null){
			s = s+"y= "+y.toString();
		}
		if( direction != null){
			s = s+"direction= "+direction.toString();
		}
		if( laplacian != null){
			s = s+"laplacian= "+laplacian.toString();
		}
		if( hessian != null){
			s = s+"hessian= "+hessian.toString();
		}
		if( size != null){
			s = s+"size= "+size.toString();
		}
		if( scale != null){
			s = s+"scale= "+scale.toString();
		}
		if( descriptors != null){
			s = s+"descriptors= ";
			for( int i=0; i<descriptorLength; i++){
				s = s+descriptors[i]+" ";
			}
		}
		
		return s;
	}
	
}