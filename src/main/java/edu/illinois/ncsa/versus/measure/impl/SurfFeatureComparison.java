/**
 * 
 */
package edu.illinois.ncsa.versus.measure.impl;

import java.lang.Double;
import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.SurfFeatureDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;

/**
 * Comparison of the SURF descriptors (set) given in each feature. This is done as described in David Lowe's paper with Euclidean distance.  
 * 
 * @author Devin Bonnie
 */
public class SurfFeatureComparison implements Measure {


	public SimilarityNumber compare(SurfFeatureDescriptor feature1, SurfFeatureDescriptor feature2) throws Exception {

		double[] f1_key      = new double[feature1.getKeypointLength()];
		double[] f2_key      = new double[feature2.getKeypointLength()];
		float[] f1_desc      = new float[feature1.getDescriptorLength()];
		float[] f2_desc      = new float[feature2.getDescriptorLength()];
		boolean[] matches    = new boolean[feature1.getLength()];
		double threshold     = 0.8;
		double matchCount    = 0;

		for( int i=0; i<feature1.getLength(); i++ ){
			
			f1_key           = feature1.getKeypoint(i);
			f1_desc          = feature1.getDescriptor(i);			
			double[] minDist = {Double.MAX_VALUE,Double.MAX_VALUE};
			
			for( int j=0; j<feature2.getLength(); j++ ){
				
				f2_key      = feature2.getKeypoint(j);
				f2_desc     = feature2.getDescriptor(j);
				double dist = 0;
				
				if( f1_key[3] == f2_key[3] ){//check if laplacians match
					
					//euclidean distance: compare the descriptors					
					for( int k=0; k<f1_desc.length; k++ ){
						dist += Math.pow((double)f1_desc[k] - (double)f2_desc[k], 2);
					}					
					dist = Math.sqrt(dist);
					
					if( dist < minDist[0]){
						minDist[0] = dist;
					}
					else if( dist < minDist[1] ){
						minDist[1] = dist;
					}
				}				
			}//end j

			if( minDist[0] / minDist[1] < threshold ){
				matches[i] = true;
				matchCount++;
			}			
		}//end i

		return new SimilarityNumber( (double)(matchCount / feature1.getLength()) );
	}
	
	
	@Override
	public String getFeatureType() {
		return SurfFeatureDescriptor.class.getName();
	}

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub		
		//this measure is already normalized
		return null;
	}
	
	
	@Override
	public String getName() {
		return "Surf Feature Comparison (Euclidean Distance)";
	}

	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)
			throws Exception {

		if (feature1 instanceof SurfFeatureDescriptor && feature2 instanceof SurfFeatureDescriptor) {
			SurfFeatureDescriptor surfFeature1 = (SurfFeatureDescriptor) feature1;
			SurfFeatureDescriptor surfFeature2 = (SurfFeatureDescriptor) feature2;
			return compare(surfFeature1, surfFeature2);
		} 
		else {
			throw new UnsupportedTypeException(
					"Similarity measure expects feature of type SurfFeatureDescriptor");
		}
	}

	@Override
	public Class<SurfFeatureComparison> getType() {
		return SurfFeatureComparison.class;
	}

}
