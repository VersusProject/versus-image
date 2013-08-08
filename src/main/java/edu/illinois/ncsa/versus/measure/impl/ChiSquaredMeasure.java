package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.utility.HasCategory;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;
import java.util.HashSet;
import java.util.Set;


/**
 * Chi-Squared distance (Neyman's)
 * 
 * @author Devin Bonnie
 */
public class ChiSquaredMeasure implements Measure, HasCategory {
	
	/**
	 * Compares two normalized RGB Histograms using the Chi-Squared metric, i.e., X^2( A, B )
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
		double X2         = 0;
		
		for( int i=0; i < normHist1[0].length; i++ ){			
			for( int j=0; j < normHist1.length; j++ ){				
				
				double Mi = ( normHist1[j][i] + normHist2[j][i] ) / 2.0;
				
				//Neyman's Chi-Squared ignores zero bins
				if( Mi != 0 ){
					X2 +=  Math.pow( normHist1[j][i] - Mi, 2 ) / Mi;
				}
			}
		}	
		return new SimilarityNumber(X2);
	}
	
	/**
	 * Compares two normalized Grayscale Histograms using the Chi-Squared metric, i.e., X^2( A, B )
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
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
		double X2       = 0;
		
		for( int j=0; j < normHist1.length; j++ ){				
			
			double Mi = ( normHist1[j] + normHist2[j] ) / 2.0;
			
			//Neyman's Chi-Squared ignores zero bins
			if( Mi != 0 ){
				X2 +=  Math.pow( normHist1[j] - Mi, 2 ) / Mi;
			}
		}
		return new SimilarityNumber(X2);
	}
	
	/**
	 * Compares two Pixel Histograms using the Chi-Squared metric, i.e., X^2( A, B )
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( PixelHistogramDescriptor feature1, PixelHistogramDescriptor feature2 ) throws Exception {
		
		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			
			throw new Exception("Features must have the same length");
		}
			
		double X2 = 0;
		
		for( int i=0; i < feature1.getNumBins(); i++ ){			
			for( int j=0; j < feature1.getNumBands(); j++ ){			
					
					double Mi = ( feature1.get(i,j) + feature2.get(i,j) ) / 2.0;
					
					//Neyman's Chi-Squared ignores zero bins
					if( Mi != 0 ){
						X2 +=  Math.pow( feature1.get(i,j) - Mi, 2 ) / Mi;
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
		else if (feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {			
			return compare( (GrayscaleHistogramDescriptor) feature1, (GrayscaleHistogramDescriptor) feature2 );
		}
		else if (feature1 instanceof PixelHistogramDescriptor && feature2 instanceof PixelHistogramDescriptor) {			
			return compare( (PixelHistogramDescriptor) feature1, (PixelHistogramDescriptor) feature2 );
		}
		else {
			throw new UnsupportedTypeException("Expecting some type of histogram feature");
		}
	}
	
	@Override
	public Set<Class<? extends Descriptor>> supportedFeaturesTypes() {
        Set<Class<? extends Descriptor>> featuresTypes = new HashSet<Class<? extends Descriptor>>(3);
        featuresTypes.add(RGBHistogramDescriptor.class);
        featuresTypes.add(GrayscaleHistogramDescriptor.class);
        featuresTypes.add(PixelHistogramDescriptor.class);
        return featuresTypes;
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

    @Override
    public String getCategory() {
        return "Squared L2 family or Chi squared family";
    }
}