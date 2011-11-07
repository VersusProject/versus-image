/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.util.*;

import com.googlecode.javacpp.FloatPointer;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.HasRGBPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.Pixel;
import edu.illinois.ncsa.versus.descriptor.impl.HoughLinesDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Extract hough lines from HasPixels adapter. This is done for both RGB and grayscale images.
 * 
 * @author Devin Bonnie
 * 
 * 
 */
public class HoughLinesExtractor implements Extractor {

	@Override
	public BufferedImageAdapter newAdapter() {
		return new BufferedImageAdapter();
	}

	@Override
	public String getName() {
		return "Pixels to Hough Lines";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws Exception {
				
		HasRGBPixels hasPixels = (HasRGBPixels) adapter;			
		double[][][] pixels    = hasPixels.getRGBPixels();
		int height             = pixels.length;
		int width              = pixels[0].length;			
		int bands              = pixels[0][0].length;
		IplImage image         = cvCreateImage( cvSize(width, height), IPL_DEPTH_8U, 1);
		
		//CONVERT THE DOUBLE ARRAY TO IPLIMAGE-----------------------------------------------------------|
		if ( bands == 3 ){ 	
			IplImage bgrImage = cvCreateImage( cvSize(width, height), IPL_DEPTH_8U, 3);
			
			for (int i=0; i < height; i++){
				for (int j=0; j < width; j++){
					
					int r = (int)pixels[i][j][0];
					int g = (int)pixels[i][j][1];
					int b = (int)pixels[i][j][2];
					
					//iplimage stores pixels as BGR
					cvSet2D(bgrImage,i,j,cvScalar(b,g,r,0));					
				}
			}
			cvCvtColor(bgrImage,image,CV_BGR2GRAY);
			cvReleaseImage(bgrImage);
		}
		else if( bands == 1 ) { 				
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
		
		CvSeq cvLines                    = new CvSeq(null);
		double dRho                      = 1;
		double dTheta                    = Math.PI/180;
		int threshold                    = 127;		
		cvLines                          = cvHoughLines2(image, CvMemStorage.create(), CV_HOUGH_STANDARD, dRho, dTheta, threshold, 0, 0 );
		ArrayList<Pixel[]> lineEndpoints = new ArrayList<Pixel[]>();
		
		for(int index=0; index<cvLines.total(); index++){
			
			FloatPointer line  = new FloatPointer(cvGetSeqElem(cvLines,index));
			double rho         = line.position(0).get();
			double theta       = line.position(1).get();
			double c           = Math.cos(theta);
			double s           = Math.sin(theta);	
			Pixel[] houghLines = new Pixel[2];
			Pixel pt1;
			Pixel pt2;
			
			if(Math.abs(c) < 0.001){
				pt1 = new Pixel( (int)Math.round(rho), 0 );
				pt2 = new Pixel( (int)Math.round(rho), height-1 );
			}
			else if(Math.abs(s) < 0.001){				
				pt1 = new Pixel( 0, (int)Math.round(rho) );
				pt2 = new Pixel( width-1, (int)Math.round(rho) );
			}
			else{
				pt1 = new Pixel( 0, (int)Math.round(rho/s) );
				pt2 = new Pixel( (int)Math.round(rho/c), 0 );
			}
			
			houghLines[0] = pt1;
			houghLines[1] = pt2;
			lineEndpoints.add(houghLines);			
		}		
		
		cvReleaseImage(image);
		
		return new HoughLinesDescriptor(lineEndpoints);
	}


	@Override
	public Set<Class<? extends Adapter>> supportedAdapters() {
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		adapters.add(HasPixels.class);
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType() {
		return HoughLinesDescriptor.class;
	}
}
