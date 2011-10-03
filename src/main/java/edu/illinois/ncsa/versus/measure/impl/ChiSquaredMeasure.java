package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;


/**
 * Chi-Squared distance (Neyman's)
 * 
 * @author Devin Bonnie
 */
public class ChiSquaredMeasure implements Measure {
	
	/**
	 * Compares two DoubleArrayFeatures using the Chi-Squared metric, i.e., X^2( A, B )
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
		}
			
		double X2 = 0;
		
		for( int i=0; i < feature1.getNumBins(); i++ ){
			
			for( int j=0; j < feature1.getNumBins(); j++ ){
				
				for( int k=0; k < feature1.getNumBins(); k++ ){
					
					double Mi = ( feature1.get(i,j,k) + feature2.get(i,j,k) ) / 2.0;
					
					//Neyman's Chi-Squared ignores zero bins
					if( Mi != 0 ){
						X2 +=  Math.pow( feature1.get(i,j,k) - Mi, 2 ) / Mi;
					}
					
				}
			}
		}		
		
		return new SimilarityNumber(X2);
	}
		
	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {
		
		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			
			return compare( (RGBHistogramDescriptor) feature1, (RGBHistogramDescriptor) feature2 );
		}
		else {
			throw new UnsupportedTypeException("Expecting some type of histogram feature");
		}
	}
	
	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}	
	
	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		return "Chi-Squared (Neyman's) Measure";
	}
	
	@Override
	public Class<ChiSquaredMeasure> getType() {
		return ChiSquaredMeasure.class;
	}
}