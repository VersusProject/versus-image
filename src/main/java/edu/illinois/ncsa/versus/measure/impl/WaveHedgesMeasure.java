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
 * Wave Hedges measure (histogram intersection family) between two color histograms.
 * 
 * @author Devin Bonnie
 * 
 */
public class WaveHedgesMeasure implements Measure {

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub		
		return null;
	}

	/**
	 * Compares two RGB Histograms with Wave Hedges. 
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
		double sum[]      = {0,0,0};
		
		for (int x=0; x < normHist2.length; x++) {			
			for (int y=0; y < normHist2[0].length; y++){
				
				if( !( (normHist1[x][y] == 0) && (normHist2[x][y] == 0) ) ){
					sum[y] += (double)Math.abs( normHist1[x][y] - normHist2[x][y] ) / (double)Math.max( normHist1[x][y], normHist2[x][y] );
				}
			}
		}
		double result = sum[0] + sum[1] + sum[2];
		return new SimilarityNumber(result);
	}
	
	/**
	 * Compares two Grayscale Histograms with Wave Hedges.
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
		double sum      = 0;
		
		for (int x=0; x < normHist1.length; x++) {
					
			if( !( (normHist1[x] == 0) && (normHist2[x] == 0) ) ){
				sum += (double)Math.abs( normHist1[x] - normHist2[x] ) / (double)Math.max( normHist1[x], normHist2[x] );
			}
		}		
		return new SimilarityNumber(sum);
	}

	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}

	@Override
	public String getName() {
		return "Wave Hedges (Intersection Family)";
	}

	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {

		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			return compare((RGBHistogramDescriptor)feature1, (RGBHistogramDescriptor)feature2);
		} 
		else if( feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {
			return compare((GrayscaleHistogramDescriptor)feature1, (GrayscaleHistogramDescriptor)feature2);
		}
		else {
			throw new UnsupportedTypeException("Similarity measure expects feature of type HistogramFeature");
		}
	}

	@Override
	public Class<WaveHedgesMeasure> getType() {
		return WaveHedgesMeasure.class;
	}

}
