/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.util.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.HasRGBPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.HarrisCornerDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Extract harris corners from HasPixels adapter. This is done for both RGB and grayscale images. 
 * 
 * @author Devin Bonnie
 * 
 * 
 */
public class HarrisCornerExtractor implements Extractor {

	@Override
	public BufferedImageAdapter newAdapter() {
		return new BufferedImageAdapter();
	}

	@Override
	public String getName() {
		return "Pixels to Harris Corners";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws Exception {
				
		HasRGBPixels hasPixels  = (HasRGBPixels) adapter;			
		double[][][] pixels     = hasPixels.getRGBPixels();
		int height              = pixels.length;
		int width               = pixels[0].length;			
		int bands               = pixels[0][0].length;
		IplImage image          = cvCreateImage( cvSize(width, height), IPL_DEPTH_8U, 1);
		IplImage cornerDst      = cvCreateImage( cvGetSize(image), IPL_DEPTH_32F, 1);
		
		//Harris Corner Detector Parameters
		int blockSize    = 5;
		int apertureSize = 3;
		double k         = 0.04; //use 0.04 - 0.15
		
		//CONVERT THE DOUBLE ARRAY TO IPLIMAGE-----------------------------------------------------------|
		//rgb image, set each RGB pixel to BGR format for iplimage, then convert to grayscale
		if ( bands == 3 ){ 	
			
			IplImage bgrImage = cvCreateImage( cvSize(width, height), IPL_DEPTH_8U, 3);
			
			for (int i=0; i < height; i++){
				for (int j=0; j < width; j++){
					
					int r = (int)pixels[i][j][0];
					int g = (int)pixels[i][j][1];
					int b = (int)pixels[i][j][2];
					
					//iplimage stores pixels as BGR
					cvSet2D(image,i,j,cvScalar(b,g,r,0));					
				}
			}
			cvCvtColor(bgrImage,image,CV_BGR2GRAY);
			cvReleaseImage(bgrImage);
		}
		//grayscale image to iplimage 
		else if( bands == 1 ) { 
			
			image = cvCreateImage( cvSize(width, height), IPL_DEPTH_8U, 1);
			
			for (int i=0; i<height; i++){
				for (int j=0; j<width; j++){	

					int pix = (int)pixels[i][j][0];
					cvSet2D(image,i,j,cvScalar(pix,0,0,0));	
				}
			}
		}
		else{
			throw new Exception("Unsupported double array image representation");
		}
		//END CONVERSION---------------------------------------------------------------------------------|
		
		//get the Harris corners via javacv
		cvCornerHarris(image, cornerDst, blockSize, apertureSize, k);			
		
		//convert the iplimage to a double array and pass that to the descriptor; the measure can worry about the threshold. 
		double[][] hc = new double[cornerDst.width()][cornerDst.height()];
		
		for( int i=0; i<cornerDst.width(); i++){
			for( int j=0; j<cornerDst.height(); j++){
				
				CvScalar pt = cvGet2D(image,i,j);
				hc[i][j]    = pt.getVal(0);
			}			
		}
	
		cvReleaseImage(image);
		cvReleaseImage(cornerDst);
		
		return new HarrisCornerDescriptor(hc);
	}


	@Override
	public Set<Class<? extends Adapter>> supportedAdapters() {
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		adapters.add(HasPixels.class);
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType() {
		return HarrisCornerDescriptor.class;
	}
}
