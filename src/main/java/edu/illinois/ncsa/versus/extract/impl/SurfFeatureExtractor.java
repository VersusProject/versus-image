/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.nio.FloatBuffer;
import java.util.*;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_features2d.*;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.HasRGBPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.SurfFeatureDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.SurfFeatureDescriptor.SurfPoint;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Extract sift features from HasPixels adapter. This is done for both RGB and grayscale images. The code before the sift
 * feature extraction can be used as a general method from a buffered image to an IPLIMAGE
 * 
 * @author Devin Bonnie
 * 
 * 
 */
public class SurfFeatureExtractor implements Extractor {

	@Override
	public BufferedImageAdapter newAdapter() {
		return new BufferedImageAdapter();
	}

	@Override
	public String getName() {
		return "Pixels to SURF Features";
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
		//rgb image, set each RGB pixel to BGR format for iplimage
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
		}
		//grayscale image to iplimage 
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
		
		//extract the surf descriptors / keypoints...
		CvSeq cvKeypoints   = new CvSeq(null);
		CvSeq cvDescriptors = new CvSeq(null);
		CvSURFParams params = cvSURFParams(500, 0);//default parameters		
		cvExtractSURF( image, null, cvKeypoints, cvDescriptors, CvMemStorage.create(), params, 0);
	

		ArrayList<SurfPoint> points = new ArrayList<SurfPoint>();
		
		for(int index=0; index<cvKeypoints.elem_size(); index++){

			CvSURFPoint cvPoint = new CvSURFPoint(cvGetSeqElem(cvKeypoints, index));		
			double info[]       = {cvPoint.pt().x(), cvPoint.pt().y(), cvPoint.dir(), cvPoint.laplacian(), cvPoint.hessian(), cvPoint.size()};
			
			FloatBuffer objectDescriptors = cvGetSeqElem(cvDescriptors, index).capacity(cvDescriptors.elem_size()).asByteBuffer().asFloatBuffer();	
		    float[] floatArray            = new float[objectDescriptors.limit()];
		    objectDescriptors.get(floatArray);

		    SurfPoint pt = new SurfPoint(info, floatArray);
		    points.add(pt);
		}
		
		cvReleaseImage(image);
		return new SurfFeatureDescriptor(points);
	}


	@Override
	public Set<Class<? extends Adapter>> supportedAdapters() {
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		adapters.add(HasPixels.class);
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType() {
		return SurfFeatureDescriptor.class;
	}
}
