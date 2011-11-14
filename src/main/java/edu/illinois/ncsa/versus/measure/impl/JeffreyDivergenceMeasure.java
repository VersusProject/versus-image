package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;


/**
 * Jeffrey Divergence of two features. 
 * 
 * The Jeffrey Divergence is a (modified) form of the K-L divergence, but is a symmetric measure.
 * 
 * @author Devin Bonnie
 */
public class JeffreyDivergenceMeasure implements Measure {
	
	/**
	 * Compares two RGB Histograms using the Jeffrey divergence i.e., d_KL( A, B )
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( RGBHistogramDescriptor feature1, RGBHistogramDescriptor feature2 ) throws Exception {
		
		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {			
			throw new Exception("Features must have the same length");
		}
		
		int[][] normHist1 = feature1.computeNormalizedHistogram();
		int[][] normHist2 = feature2.computeNormalizedHistogram();		
		double dist       = 0;
		double max1       = 0;
		double max2       = 0;		
		
		for( int i=0; i < normHist1[0].length; i++ ){
			
			for( int j=0; j < normHist1.length; j++ ){					
				max1 += normHist1[j][i];
				max2 += normHist2[j][i];				
			}		

			for( int j=0; j < normHist1.length; j++ ){					
				//ignore zero bins
				if( (normHist1[j][i] != 0.0) && (normHist2[j][i] != 0.0) ){						
					
					double a  = (double)normHist1[j][i] / max1;
					double b  = (double)normHist2[j][i] / max2;					
					double Mi = ( a + b ) / 2;					
					dist     += a * Math.log( a / Mi ) + b * Math.log( b / Mi );
				}
			}
			max1 = 0;
			max2 = 0;
		}		
		return new SimilarityNumber(dist);
	}
	/**
	 * Compares two Grayscale Histograms using the Jeffrey divergence i.e., d_KL( A, B )
	 * 
	 * @param GrayscaleHistogramDescriptor
	 * @param GrayscaleHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( GrayscaleHistogramDescriptor feature1, GrayscaleHistogramDescriptor feature2 ) throws Exception {
		
		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			throw new Exception("Features must have the same length");
		}
		
		int[] normHist1 = feature1.computeNormalizedHistogram();
		int[] normHist2 = feature2.computeNormalizedHistogram();		
		double dist     = 0;
		double max1     = 0;
		double max2     = 0;		
		
	
		for( int j=0; j < normHist1.length; j++ ){					
			max1 += normHist1[j];
			max2 += normHist2[j];				
		}		

		for( int j=0; j < normHist1.length; j++ ){					
			//ignore zero bins
			if( (normHist1[j] != 0.0) && (normHist2[j] != 0.0) ){						
				
				double a  = (double)normHist1[j] / max1;
				double b  = (double)normHist2[j] / max2;					
				double Mi = ( a + b ) / 2;					
				dist     += a * Math.log( a / Mi ) + b * Math.log( b / Mi );
			}
		}					
		return new SimilarityNumber(dist);
	}
	
	
	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {
		
		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {			
			return compare( (RGBHistogramDescriptor) feature1, (RGBHistogramDescriptor) feature2 );
		}
		else if (feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {			
			return compare( (GrayscaleHistogramDescriptor) feature1, (GrayscaleHistogramDescriptor) feature2 );
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
		//simply divide by the length of 'numBins' in the histogram to normalize the Jeffrey divergence...
		return null;
	}
	
	@Override
	public String getName() {
		return "Jeffrey Divergence";
	}
	
	@Override
	public Class<JeffreyDivergenceMeasure> getType() {
		return JeffreyDivergenceMeasure.class;
	}
}