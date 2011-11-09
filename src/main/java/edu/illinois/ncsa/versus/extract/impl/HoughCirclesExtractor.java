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
import edu.illinois.ncsa.versus.descriptor.impl.Pixel;
import edu.illinois.ncsa.versus.descriptor.impl.HoughCirclesDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Extract Hough circles from HasPixels adapter. This is done for both RGB and grayscale images.
 * 
 * @author Devin Bonnie
 * 
 * 
 */
public class HoughCirclesExtractor implements Extractor {

	@Override
	public BufferedImageAdapter newAdapter() {
		return new BufferedImageAdapter();
	}

	@Override
	public String getName() {
		return "Pixels to Hough Circles";
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

		//First smooth the image (recommended in order to reduce false positives). 
		cvSmooth(image,image,CV_GAUSSIAN,9,0,0,0);
		
		//Hough Circles Parameters
		double dp                 = 2; //accumulator resolution: if it is 1, accumulator will have the same input resolution, if 2, accumulator will have twice smaller width and height
		double minDist            = 50; //minimum distance between detected circles centers
		double param1             = 100; //the higher threshold of the two passed to Canny edge detector (the lower one will be twice smaller)
		double param2             = 200; //accumulator threshold at the center detection stage
		int minRadius             = Math.min( (int)Math.round(0.10*width), (int)Math.round(0.10*height) ); //minimum radius of circles to search for
		int maxRadius             = Math.max(width, height); //maximum radius of circles to search for
		CvSeq cvCircles           = new CvSeq(null);		
		cvCircles                 = cvHoughCircles(image, CvMemStorage.create(), CV_HOUGH_GRADIENT, dp, minDist, param1, param2, minRadius, maxRadius );
		ArrayList<Pixel> hCircles = new ArrayList<Pixel>();
		
		for( int i=0; i<cvCircles.total(); i++){
			
			CvPoint3D32f info = new CvPoint3D32f(cvGetSeqElem(cvCircles,i));
			int x             = Math.round( info.x() );
			int y             = Math.round( info.y() );
			int radius        = Math.round( info.z() );
			hCircles.add( new Pixel(x,y,radius));
		}
	
		cvReleaseImage(image);
		return new HoughCirclesDescriptor(hCircles);
	}


	@Override
	public Set<Class<? extends Adapter>> supportedAdapters() {
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		adapters.add(HasPixels.class);
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType() {
		return HoughCirclesDescriptor.class;
	}
}
