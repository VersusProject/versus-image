/**
 * 
 */
package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.utility.HasCategory;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;
import java.util.HashSet;
import java.util.Set;

/**
 * Euclidean distance between two pixel histograms.
 * 
 * @author Luigi Marini, Devin Bonnie
 * 
 */
public class HistogramDistanceMeasure implements Measure {

	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Compares two Pixel Histograms by Euclidean Distance.
	 * 
	 * @param PixelHistogramDescriptor
	 * @param PixelHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	public SimilarityNumber compare(PixelHistogramDescriptor feature1,
			PixelHistogramDescriptor feature2) throws Exception {

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
	
	/**
	 * Compares two RGB Histograms by Euclidean Distance.
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	public SimilarityNumber compare(RGBHistogramDescriptor feature1, RGBHistogramDescriptor feature2) throws Exception {

		double sum = 0;
		
		for (int x=0; x<feature1.getNumBands(); x++) {
			for (int y=0; y<feature1.getNumBins(); y++) {			
					
				sum += Math.pow(feature1.get(y, x) - feature2.get(y, x), 2);
			}	
		}	
		return new SimilarityNumber(sum);
	}
	
	/**
	 * Compares two Grayscale Histograms by Euclidean Distance.
	 * 
	 * @param GrayscaleHistogramDescriptor
	 * @param GrayscaleHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	public SimilarityNumber compare(GrayscaleHistogramDescriptor feature1, GrayscaleHistogramDescriptor feature2) throws Exception {

		double sum = 0;
		
		for (int x=0; x<feature1.getNumBins(); x++) {
				
			sum += Math.pow(feature1.get(x) - feature2.get(x), 2);
		}	
		return new SimilarityNumber(sum);
	}
	
	@Override
	public Set<Class<? extends Descriptor>> supportedFeaturesTypes() {
        Set<Class<? extends Descriptor>> featuresTypes = new HashSet<Class<? extends Descriptor>>(3);
        featuresTypes.add(PixelHistogramDescriptor.class);
        featuresTypes.add(RGBHistogramDescriptor.class);
        featuresTypes.add(GrayscaleHistogramDescriptor.class);
        return featuresTypes;
	}

	@Override
	public String getName() {
		return "Histogram Euclidean Distance";
	}

	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)
			throws Exception {

		if (feature1 instanceof PixelHistogramDescriptor
				&& feature2 instanceof PixelHistogramDescriptor) {
			PixelHistogramDescriptor histogramFeature1 = (PixelHistogramDescriptor) feature1;
			PixelHistogramDescriptor histogramFeature2 = (PixelHistogramDescriptor) feature2;
			return compare(histogramFeature1, histogramFeature2);
		} 
		else if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			
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
	public Class<HistogramDistanceMeasure> getType() {
		return HistogramDistanceMeasure.class;
	}

}
