/**
 * 
 */
package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;

/**
 * Bhattacharyya Distance of two histograms. Only works with normalized histograms and does not work well for sparse histograms.  
 * 
 * @author Devin Bonnie
 * 
 */
public class BhattacharyyaDistanceMeasure implements Measure {

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub		
		//normalize by the number of bins, i.e., 1/n
		return null;
	}

	/**
	 * Compares two RGB Histograms using the Bhattacharyya Distance metric.
	 * 
	 * @param feature1: RGBHistogramDescriptor
	 * @param feature2: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	public SimilarityNumber compare(RGBHistogramDescriptor feature1, RGBHistogramDescriptor feature2) throws Exception {

		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			
			throw new Exception("Features must have the same length");
		}		
		
		int[][] normHist1 = feature1.computeNormalizedHistogram();
		int[][] normHist2 = feature2.computeNormalizedHistogram();
		double sum        = 0;
		
		for (int i=0; i < normHist1[0].length; i++) {
			
			for (int j=0; j < normHist1.length; j++) {
										
				sum += Math.sqrt( (double)normHist1[j][i] * (double)normHist2[j][i] ); 					
			}	
		}

		return new SimilarityNumber( sum  );
	}
	
	/**
	 * Compares two Grayscale Histograms using the Bhattacharyya Distance metric.
	 * 
	 * @param feature1: RGBHistogramDescriptor
	 * @param feature2: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	public SimilarityNumber compare(GrayscaleHistogramDescriptor feature1, GrayscaleHistogramDescriptor feature2) throws Exception {

		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			
			throw new Exception("Features must have the same length");
		}		
		
		int[] normHist1 = feature1.computeNormalizedHistogram();
		int[] normHist2 = feature2.computeNormalizedHistogram();
		double sum      = 0;
		
		for (int i=0; i < normHist1.length; i++) {
										
			sum += Math.sqrt( (double)normHist1[i] * (double)normHist2[i] );				
		}

		return new SimilarityNumber( sum  );
	}
	
	/**
	 * Compares two Pixel Histograms using the Bhattacharyya Distance metric.
	 * 
	 * @param feature1: RGBHistogramDescriptor
	 * @param feature2: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	public SimilarityNumber compare(PixelHistogramDescriptor feature1, PixelHistogramDescriptor feature2) throws Exception {

		// Check feature lengths, they must be equal
		if( feature1.getNumBins() != feature2.getNumBins() ) {
			
			throw new Exception("Features must have the same length");
		}		
		
		double sum = 0;
		
		for (int x=0; x<feature1.getNumBins(); x++) {
			
			for (int y=0; y<feature1.getNumBins(); y++) {
				
				for (int z=0; z<feature1.getNumBins(); z++) {
					
					sum += Math.sqrt( (double)feature1.get(x, y, z) * (double)feature2.get(x ,y, z) ); 					
				}	
			}	
		}

		return new SimilarityNumber( sum  );
	}
	
	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}

	@Override
	public String getName() {
		return "Bhattacharyya Distance";
	}

	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {

		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			RGBHistogramDescriptor histogramFeature1 = (RGBHistogramDescriptor) feature1;
			RGBHistogramDescriptor histogramFeature2 = (RGBHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		} 
		else if (feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {
			GrayscaleHistogramDescriptor histogramFeature1 = (GrayscaleHistogramDescriptor) feature1;
			GrayscaleHistogramDescriptor histogramFeature2 = (GrayscaleHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		} 
		else if (feature1 instanceof PixelHistogramDescriptor && feature2 instanceof PixelHistogramDescriptor) {
			PixelHistogramDescriptor histogramFeature1 = (PixelHistogramDescriptor) feature1;
			PixelHistogramDescriptor histogramFeature2 = (PixelHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		} 
		else {
			throw new UnsupportedTypeException("Similarity measure expects feature of type HistogramFeature");
		}
	}

	@Override
	public Class<BhattacharyyaDistanceMeasure> getType() {
		return BhattacharyyaDistanceMeasure.class;
	}

}
