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
 * Histogram intersection for grayscale and RGB histograms.
 * 
 * @author Devin Bonnie
 * 
 */
public class HistogramIntersectionMeasure implements Measure {

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub		
		return null;
	}

	/**
	 * Compares two RGB Histograms by Histogram Intersection. 
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
		int sumR          = 0;
		int sumG          = 0;
		int sumB          = 0;
		
		for (int x=0; x < normHist2.length; x++) {
					
			sumR += Math.min( normHist1[x][0], normHist2[x][0] );
			sumG += Math.min( normHist1[x][1], normHist2[x][1] );
			sumB += Math.min( normHist1[x][2], normHist2[x][2] );
		}
		return new SimilarityNumber(sumR+sumG+sumB);
	}
	
	/**
	 * Compares two Grayscale Histograms by Histogram Intersection.
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
		int sum         = 0;
		
		for (int x=0; x < normHist1.length; x++) {
					
			sum += Math.min( normHist1[x], normHist2[x] );
		}		
		return new SimilarityNumber(sum);
	}

	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}

	@Override
	public String getName() {
		return "Histogram Intersection";
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
	public Class<HistogramIntersectionMeasure> getType() {
		return HistogramIntersectionMeasure.class;
	}

}
