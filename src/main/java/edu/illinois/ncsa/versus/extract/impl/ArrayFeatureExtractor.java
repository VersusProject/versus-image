/**
 * 
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.util.HashSet;
import java.util.Set;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.ThreeDimensionalDoubleArrayFeature;
import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * @author Luigi Marini
 * 
 */
public class ArrayFeatureExtractor implements Extractor {

	private ThreeDimensionalDoubleArrayFeature extract(HasPixels adapter) {
		return new ThreeDimensionalDoubleArrayFeature(adapter.getRGBPixels());
	}

	@Override
	public ImageObjectAdapter newAdapter() {
		return new ImageObjectAdapter();
	}

	@Override
	public String getName() {
		return "Pixels to Array";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws UnsupportedTypeException {
		if (adapter instanceof HasPixels) {
			HasPixels hasPixels = (HasPixels) adapter;
			return extract(hasPixels);
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
		return ThreeDimensionalDoubleArrayFeature.class;
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
