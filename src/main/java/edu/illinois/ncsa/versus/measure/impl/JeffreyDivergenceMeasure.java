package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;


/**
 * Kullback-Leibler Divergence of two features.
 * 
 * @author Devin Bonnie
 */
public class JeffreyDivergenceMeasure implements Measure {
	
	/**
	 * Compares two RGB Histograms using the Jeffrey divergence i.e., d_KL( A, B )
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
		
		int[][] normHist1 = feature1.computeNormalizedHistogram();
		int[][] normHist2 = feature2.computeNormalizedHistogram();		
		double dist       = 0;
		
		for( int i=0; i < normHist1[0].length; i++ ){
			
			for( int j=0; j < normHist1.length; j++ ){					
				//ignore zero bins
				if( (normHist1[j][i] != 0.0) && (normHist2[j][i] != 0.0) ){						
					
					double Mi = ( (double)normHist1[j][i] + (double)normHist2[j][i] ) / 2;
					
					dist += (double)normHist1[j][i] * Math.log( (double)normHist2[j][i] / Mi ) + (double)normHist2[j][i] * Math.log( (double)normHist2[j][i] / Mi );
				}
			}	
		}		
		
		return new SimilarityNumber(dist);
	}
	/**
	 * Compares two Grayscale Histograms using the Jeffrey divergence i.e., d_KL( A, B )
	 * 
	 * @param feature1 A: RGBHistogramDescriptor
	 * @param feature2 B: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( GrayscaleHistogramDescriptor feature1, GrayscaleHistogramDescriptor feature2 ) throws Exception {
		
		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			
			throw new Exception("Features must have the same length");
			//return null?
		}
		
		int[] normHist1 = feature1.computeNormalizedHistogram();
		int[] normHist2 = feature2.computeNormalizedHistogram();		
		double dist     = 0;
		
		for( int i=0; i < normHist1.length; i++ ){							
			//ignore zero bins
			if( (normHist1[i] != 0.0) && (normHist2[i] != 0.0) ){						
				
				double Mi = ( (double)normHist1[i] + (double)normHist2[i] ) / 2;
				
				dist += (double)normHist1[i] * Math.log( (double)normHist2[i] / Mi ) + (double)normHist2[i] * Math.log( (double)normHist2[i] / Mi );
			}
		}		
		
		return new SimilarityNumber(dist);
	}
	
	/**
	 * Compares two Pixel Histograms using the Jeffrey divergence i.e., d_KL( A, B )
	 * 
	 * @param feature1 A: RGBHistogramDescriptor
	 * @param feature2 B: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( PixelHistogramDescriptor feature1, PixelHistogramDescriptor feature2 ) throws Exception {
		
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
						
						double Mi = ( (double)feature1.get(i,j,k) + (double)feature2.get(i,j,k) ) / 2;
						
						dist += (double)feature1.get(i,j,k) * Math.log( (double)feature1.get(i,j,k) / Mi ) + (double)feature2.get(i,j,k) * Math.log( (double)feature2.get(i,j,k) / Mi );
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
		else if (feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {
			
			return compare( (GrayscaleHistogramDescriptor) feature1, (GrayscaleHistogramDescriptor) feature2 );
		}
		else if (feature1 instanceof PixelHistogramDescriptor && feature2 instanceof PixelHistogramDescriptor) {
			
			return compare( (PixelHistogramDescriptor) feature1, (PixelHistogramDescriptor) feature2 );
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