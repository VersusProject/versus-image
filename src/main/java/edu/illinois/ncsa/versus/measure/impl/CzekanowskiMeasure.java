/**
 * 
 */
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
 * Czekanowski Measure between two color histograms.
 * 
 * @author Devin Bonnie
 * 
 */
public class CzekanowskiMeasure implements Measure {

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub		
		//normalize by the number of bins, i.e., 1/n
		return null;
	}

	/**
	 * Compares two RGB Histograms by Czekanowski Measure. 
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
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
		double s1         = 0;
		double s2         = 0;
		
		for (int i=0; i < normHist1.length; i++){
			for (int j=0; j < normHist1[0].length; j++){

				s1 += Math.abs( normHist1[i][j] - normHist2[i][j] );
				s2 += normHist1[i][j] + normHist2[i][j];
			}
		}
		return new SimilarityNumber(s1/s2);
	}
	
	/**
	 * Compares two Grayscale Histograms by Czekanowski Measure.
	 * 
	 * @param GrayscaleHistogramDescriptor
	 * @param GrayscaleHistogramDescriptor
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
		double s1       = 0;
		double s2       = 0;
		
		for (int i=0; i < normHist1.length; i++){
			
			s1 += Math.abs( normHist1[i] - normHist2[i] );
			s2 += normHist1[i] + normHist2[i];
		}
		return new SimilarityNumber(s1/s2);
	}

	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}

	@Override
	public String getName() {
		return "Czekanowski Measure";
	}

	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {

		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			
			RGBHistogramDescriptor histogramFeature1 = (RGBHistogramDescriptor) feature1;
			RGBHistogramDescriptor histogramFeature2 = (RGBHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		} 
		else if( feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {
			
			GrayscaleHistogramDescriptor histogramFeature1 = (GrayscaleHistogramDescriptor) feature1;
			GrayscaleHistogramDescriptor histogramFeature2 = (GrayscaleHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		}
		else {
			throw new UnsupportedTypeException(
					"Similarity measure expects feature of type HistogramFeature");
		}
	}

	@Override
	public Class<CzekanowskiMeasure> getType() {
		return CzekanowskiMeasure.class;
	}

}
