package edu.illinois.ncsa.versus.measure.impl;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;
import java.util.Arrays;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

/**
 * Chi-Squared distance (Neyman's)
 * 
 * @author Devin Bonnie
 */
public class EarthMoversDistanceMeasure implements Measure {
	
	/**
	 * Compares two grayscale Histograms using the Earth Mover's Distance
	 * 
	 * @param feature1 A: RGBHistogramDescriptor
	 * @param feature2 B: RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( GrayscaleHistogramDescriptor feature1, GrayscaleHistogramDescriptor feature2 ) throws Exception {
		
		//the histograms / arrays to be compared do not need to be of the same length
		CvMat matf1     = CvMat.create(feature1.getNumBins(), 2, CV_32FC1 ); //store as weight, point
		CvMat matf2     = CvMat.create(feature2.getNumBins(), 2, CV_32FC1 );
		int[] hist1     = feature1.getHistogram();
		int[] hist2     = feature2.getHistogram();		
		
		//convert the grayscale histograms to cvMat ----------------------------------------|
		for (int i=0; i<hist1.length; i++){			
			
			matf1.put(i,0,(double)hist1[i]);
			matf1.put(i,1,i);
		}	
		for (int i=0; i<hist2.length; i++){			
			matf2.put(i,0,(double)hist2[i]);
			matf2.put(i,1,i);
		}	
		//end conversion -------------------------------------------------------------------|
		
		//call openCV EMD measure -- see http://opencv.willowgarage.com/documentation/python/structural_analysis_and_shape_descriptors.html
		float dist = cvCalcEMD2(matf1, matf2, CV_DIST_L2, null, null, null, null, null);		
		
		return new SimilarityNumber( (double)dist );
	}
	

	//TODO RGB, PIXEL histograms
	
	@Override
	public Similarity compare(Descriptor feature1, Descriptor feature2)	throws Exception {
		
		if (feature1 instanceof RGBHistogramDescriptor && feature2 instanceof RGBHistogramDescriptor) {
			
			return compare( (RGBHistogramDescriptor) feature1, (RGBHistogramDescriptor) feature2 );
		}
		else if (feature1 instanceof GrayscaleHistogramDescriptor && feature2 instanceof GrayscaleHistogramDescriptor) {
			
			return compare( (GrayscaleHistogramDescriptor) feature1, (GrayscaleHistogramDescriptor) feature2 );
		}
		else {
			throw new UnsupportedTypeException("Expecting some type of histogram feature");
		}
	}
	
	@Override
	public String getFeatureType() {
		return RGBHistogramDescriptor.class.getName();
	}	
	
	@Override
	public SimilarityPercentage normalize(Similarity similarity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		return "Earth Mover's Distance";
	}
	
	@Override
	public Class<EarthMoversDistanceMeasure> getType() {
		return EarthMoversDistanceMeasure.class;
	}
}