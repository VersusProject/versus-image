/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.ColorLayoutFeature;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Create a {@linkplain ColorLayoutFeature} from a object that has pixels and
 * implements the {@linkplain HasPixel} interface.
 * 
 * @author Luigi Marini
 * 
 */
public class SignatureVectorExtraction implements Extractor {

	@Override
	public HasPixels newAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Pixel to Signature Vector";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws Exception {
		if (adapter instanceof HasPixels) {
			HasPixels hasPixels = (HasPixels) adapter;
			double[][][] pixels = hasPixels.getRGBPixels();
			ColorLayoutFeature signatureVector = new ColorLayoutFeature(
					pixels.length, pixels[0].length);
			for (int x = 0; x < pixels.length; x++) {
				for (int y = 0; y < pixels[0].length; y++) {
					Color color = new Color((float) pixels[x][y][0],
							(float) pixels[x][y][1], (float) pixels[x][y][2]);
					signatureVector.setSignature(color, x, y);
				}
			}
			return signatureVector;
		} else {
			throw new UnsupportedTypeException();
		}
	}

	@Override
	public Set<Class<? extends Adapter>> supportedAdapters() {
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		adapters.add(HasPixels.class);
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType() {
		return ColorLayoutFeature.class;
	}
	
	@Override
	public boolean hasPreview(){
		return false;
	}
	
	@Override
	public String previewName(){
		return null;
	}
}
