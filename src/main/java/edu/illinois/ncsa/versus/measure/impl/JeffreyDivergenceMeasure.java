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
 * Jeffrey Divergence of two features. The image histograms are modified s.t. they satisfy the requirements of a probability mass funtion.  
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
	 * @param feature1 A: RGBHistogramDescriptor
	 * @param feature2 B: RGBHistogramDescriptor
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
		}
			
		double dist = 0;
		double max1 = 0;
		double max2 = 0;
		
		for( int i=0; i < feature1.getNumBins(); i++ ){			
			for( int j=0; j < feature1.getNumBins(); j++ ){				
				for( int k=0; k < feature1.getNumBins(); k++ ){		
					max1 += feature1.get(i,j,k);
					max2 += feature2.get(i,j,k);
				}
			}
		}
		
		for( int i=0; i < feature1.getNumBins(); i++ ){			
			for( int j=0; j < feature1.getNumBins(); j++ ){				
				for( int k=0; k < feature1.getNumBins(); k++ ){	
					//ignore zero bins
					if( (feature1.get(i,j,k) != 0.0) && (feature2.get(i,j,k) != 0.0) ){		
						double a  = (double)feature1.get(i,j,k) / max1;
						double b  = (double)feature2.get(i,j,k) / max2;						
						double Mi = ( a + b ) / 2;						
						dist     += a * Math.log( a / Mi ) + b * Math.log( b / Mi );
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