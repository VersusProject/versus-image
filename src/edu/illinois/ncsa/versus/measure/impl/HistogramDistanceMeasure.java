/**
 * 
 */
package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;

/**
 * Euclidean distance between two color histograms.
 * 
 * @author Luigi Marini
 * 
 */
public class HistogramDistanceMeasure implements Measure {

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub
		return null;
	}

	public SimilarityNumber compare(RGBHistogramDescriptor feature1,
			RGBHistogramDescriptor feature2) throws Exception {

		double sum = 0;
		
		for (int x=0; x<feature1.getNumBins(); x++) {
			for (int y=0; y<feature1.getNumBins(); y++) {
				for (int z=0; z<feature1.getNumBins(); z++) {
					sum += Math.pow(feature1.get(x,y,z) - feature2.get(x,y,z), 2);
				}	
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
		return "Histogram Distance";
	}

	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)
			throws Exception {

		if (feature1 instanceof RGBHistogramDescriptor
				&& feature2 instanceof RGBHistogramDescriptor) {
			RGBHistogramDescriptor histogramFeature1 = (RGBHistogramDescriptor) feature1;
			RGBHistogramDescriptor histogramFeature2 = (RGBHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		} else {
			throw new UnsupportedTypeException(
					"Similarity measure expects feature of type HistogramFeature");
		}
	}

	@Override
	public Class<HistogramDistanceMeasure> getType() {
		return HistogramDistanceMeasure.class;
	}

}
