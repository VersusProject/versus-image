package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;


/**
 * Kullback-Leibler Divergence of two features.
 * 
 * @author Devin Bonnie
 */
public class KLdivergenceMeasure implements Measure {
	
	/**
	 * Compares two RGB Histograms using the KL divergence i.e., d_KL( A, B )
	 * 
	 * @param feature1 A: RGBHistogramDescriptor
	 * @param feature2 B: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( RGBHistogramDescriptor feature1, RGBHistogramDescriptor feature2 ) throws Exception {
		
		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			
			throw new Exception("Features must have the same length");
			//return null?
		}
			
		double dist = 0;
		
		for( int i=0; i < feature1.getNumBins(); i++ ){
			
			for( int j=0; j < feature1.getNumBins(); j++ ){
				
				for( int k=0; k < feature1.getNumBins(); k++ ){
					
					//ignore zero bins
					if( (feature1.get(i,j,k) != 0.0) && (feature2.get(i,j,k) != 0.0) ){
						dist += (double)feature1.get(i,j,k) * Math.log( (double)feature1.get(i,j,k) / (double)feature2.get(i,j,k) );
					}
				}
			}	
		}		
		
		return new SimilarityNumber(dist);
	}
	
	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {
		
		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			
			return compare( (RGBHistogramDescriptor) feature1, (RGBHistogramDescriptor) feature2 );
		}
		else {
			throw new UnsupportedTypeException("Expects a histogram");
		}
	}
	
	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}
	
	
	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub		
		//simply divide by the lenth of 'numBins' in the histogram to normalize the KL divergence...
		return null;
	}
	
	@Override
	public String getName() {
		return "Kullback-Leibler Divergence";
	}
	
	@Override
	public Class<KLdivergenceMeasure> getType() {
		return KLdivergenceMeasure.class;
	}
}