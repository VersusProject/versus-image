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

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

/**
 * Earth Mover's Distance (via openCV) for grayscale, RGB, and Pixel Histograms. 
 * 
 * @author Devin Bonnie
 */
public class EarthMoversDistanceMeasure implements Measure {
	
	/**
	 * Compares two grayscale Histograms using the Earth Mover's Distance
	 * 
	 * @param GrayscaleHistogramDescriptor
	 * @param GrayscaleHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( GrayscaleHistogramDescriptor feature1, GrayscaleHistogramDescriptor feature2 ) throws Exception {
		
		//the histograms / arrays to be compared do not need to be of the same length
		CvMat matf1 = CvMat.create(feature1.getNumBins(), 2, CV_32FC1 ); //store as weight, point
		CvMat matf2 = CvMat.create(feature2.getNumBins(), 2, CV_32FC1 );
		int[] hist1 = feature1.getHistogram();
		int[] hist2 = feature2.getHistogram();		
		
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
	
	/**
	 * Compares two RGB Histograms using the Earth Mover's Distance
	 * 
	 * @param RGBHistogramDescriptor
	 * @param RGBHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( RGBHistogramDescriptor feature1, RGBHistogramDescriptor feature2 ) throws Exception {
		
		//the histograms / arrays to be compared do not need to be of the same length
		CvMat matf1   = CvMat.create(3*feature1.getNumBins(), 3, CV_32FC1 ); //store as weight, point
		CvMat matf2   = CvMat.create(3*feature2.getNumBins(), 3, CV_32FC1 );
		int[][] hist1 = feature1.getHistogram();
		int[][] hist2 = feature2.getHistogram();		
		
		//convert the rgb histograms to cvMat ----------------------------------------|
		int index = 0;
		for (int i=0; i<hist1.length; i++){			
			for (int j=0; j<hist1[0].length; j++){
				matf1.put(index,0,(double)hist1[i][j]);
				matf1.put(index,1,i);
				matf1.put(index,2,j);
				index++;
			}
		}	
		index = 0;
		for (int i=0; i<hist2.length; i++){			
			for (int j=0; j<hist2[0].length; j++){
				matf2.put(index,0,(double)hist2[i][j]);
				matf2.put(index,1,i);
				matf2.put(index,2,j);
				index++;
			}
		}	
		//end conversion -------------------------------------------------------------|
		
		//call openCV EMD measure -- see http://opencv.willowgarage.com/documentation/python/structural_analysis_and_shape_descriptors.html
		float dist = cvCalcEMD2(matf1, matf2, CV_DIST_L2, null, null, null, null, null);		
		
		return new SimilarityNumber( (double)dist );
	}
	
	/**
	 * Compares two pixel Histograms using the Earth Mover's Distance
	 * 
	 * @param PixelHistogramDescriptor
	 * @param PixelHistogramDescriptor
	 * @return SimilarityNumber
	 * @throws Exception
	 */
	private SimilarityNumber compare( PixelHistogramDescriptor feature1, PixelHistogramDescriptor feature2 ) throws Exception {
		
		//the histograms / arrays to be compared do not need to be of the same length
		CvMat matf1     = CvMat.create( (int)Math.pow(feature1.getNumBins(),3), 4, CV_32FC1 ); //store as weight, point
		CvMat matf2     = CvMat.create( (int)Math.pow(feature2.getNumBins(),3), 4, CV_32FC1 );
		int[][][] hist1 = feature1.getHistogram();
		int[][][] hist2 = feature2.getHistogram();		
		
		//convert the pixel histograms to cvMat ----------------------------------------|
		int index = 0;
		for (int i=0; i<hist1.length; i++){			
			for (int j=0; j<hist1[0].length; j++){
				for (int k=0; k<hist1[0][0].length; k++){
					matf1.put(index,0,(double)hist1[i][j][k]);
					matf1.put(index,1,i);
					matf1.put(index,2,j);
					matf1.put(index,3,k);
					index++;
				}
			}
		}	
		index = 0;
		for (int i=0; i<hist2.length; i++){			
			for (int j=0; j<hist2[0].length; j++){
				for (int k=0; k<hist2[0][0].length; k++){
					matf2.put(index,0,(double)hist2[i][j][k]);
					matf2.put(index,1,i);
					matf2.put(index,2,j);
					matf2.put(index,3,k);
					index++;
				}
			}
		}	
		//end conversion ---------------------------------------------------------------|
		
		//call openCV EMD measure -- see http://opencv.willowgarage.com/documentation/python/structural_analysis_and_shape_descriptors.html
		float dist = cvCalcEMD2(matf1, matf2, CV_DIST_L2, null, null, null, null, null);		
		
		return new SimilarityNumber( (double)dist );
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