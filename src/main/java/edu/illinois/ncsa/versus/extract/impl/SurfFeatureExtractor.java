/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.util.HashSet;
import java.util.Set;

import java.lang.Double;

import com.googlecode.javacpp.*;
import com.googlecode.javacv.*;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_features2d.*;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.HasRGBPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.SurfFeatureDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Extract sift features from HasPixels adapter.
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
		return "Pixels to Sift Features";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws Exception {
				
		HasRGBPixels hasPixels = (HasRGBPixels) adapter;			
		double[][][] pixels    = hasPixels.getRGBPixels();
		int width              = pixels.length;
		int height             = pixels[0].length;
		int bands              = pixels[0][0].length;
		IplImage image         = cvCreateImage( cvSize(width, height), IPL_DEPTH_8U, bands);
		
		//CONVERT THE DOUBLE ARRAY TO IPLIMAGE-----------------------------------------------------------|
		//rgb image, set each RGB pixel to BGR format for iplimage
		if ( bands == 3){ 					
			for (int i=0; i < width; i++){
				for (int j=0; j < height; j++){
					
					int r = (int)pixels[i][j][0];
					int g = (int)pixels[i][j][1];
					int b = (int)pixels[i][j][2];
					
					//iplimage stores pixels as BGR
					cvSet2D(image, i, j, cvScalar(b,g,r,0) );					
				}
			}
		}
		//grayscale image, easy to serialize data and put in the byte buffer.  
		else if( bands == 1) { 			
			int index     = 0;
			byte[] bArray = new byte[width*height];
			
			for (int i=0; i<width; i++){
				for (int j=0; j<height; j++){	
					
					Double tmp    = new Double( pixels[i][j][0] );
					bArray[index] = tmp.byteValue();
					index++;
				}
			}
			image.getByteBuffer().put(bArray);
		}
		else{
			throw new Exception("Unsupported double array image representation");
		}
		//END CONVERSION---------------------------------------------------------------------------------|

		//extract the surf descriptors / keypoints...
		
		
		//store everything in a surf feature descriptor (versus representation)

		
		//deallocate CvMat or IPLIMAGE when finished extracting descriptors
		return new SurfFeatureDescriptor();
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
