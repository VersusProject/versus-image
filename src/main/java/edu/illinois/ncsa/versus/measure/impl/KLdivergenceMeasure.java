package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;
import java.util.HashSet;
import java.util.Set;


/**
 * Kullback-Leibler Divergence of two features. 
 * 
 * @author Devin Bonnie
 */
public class KLdivergenceMeasure implements Measure {
	
	/**
	 * Compares two RGB Histograms using the KL divergence i.e., d_KL( A, B )
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */	private SimilarityNumber compare( RGBHistogramDescriptor feature1, RGBHistogramDescriptor feature2 ) throws Exception {

		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {			
			throw new Exception("Features must have the same length");
		}
		
		int[][] normHist1 = feature1.computeNormalizedHistogram();
		int[][] normHist2 = feature2.computeNormalizedHistogram();
		
		double max1[]     = {0,0,0}; 
		double max2[]     = {0,0,0};
		double dist       = 0;
		
		for( int i=0; i < normHist1[0].length; i++ ){
			
			for (int x=0; x < normHist1.length; x++){
				max1[i] += normHist1[x][i];
				max2[i] += normHist2[x][i];
			}				
				
			for( int j=0; j < normHist1.length; j++ ){					
				//ignore zero bins
				if( (normHist1[j][i] != 0.0) && (normHist2[j][i] != 0.0) ){						
					
					double a = (double)normHist1[j][i] / max1[i];
					double b = (double)normHist2[j][i] / max2[i];
					
					dist += a * Math.log(  a / b );
				}			
			}			
		}
		return new SimilarityNumber(dist);
	}
	
	/**
	 * Compares two Grayscale Histograms using the KL divergence i.e., d_KL( A, B )
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
		double max1     = 0; 
		double max2     = 0;
		
		for (int i=0; i < normHist1.length; i++){
			max1 += normHist1[i];
			max2 += normHist2[i];
		}

		double dist = 0;
		
		for( int j=0; j < normHist1.length; j++ ){					
			//ignore zero bins
			if( (normHist1[j] != 0.0) && (normHist2[j] != 0.0) ){						
				
				double a = (double)normHist1[j] / max1;
				double b = (double)normHist2[j] / max2;
				
				dist += a * Math.log(  a / b );
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
	public Set<Class<? extends Descriptor>> supportedFeaturesTypes() {
        Set<Class<? extends Descriptor>> featuresTypes = new HashSet<Class<? extends Descriptor>>(2);
        featuresTypes.add(RGBHistogramDescriptor.class);
        featuresTypes.add(GrayscaleHistogramDescriptor.class);
        return featuresTypes;
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