package edu.illinois.ncsa.versus.extract.impl;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor;

public class RGBHistogramExtractorTest {

	@Test
	public void testRepeatExtraction() {
		ImageObjectAdapter adapter = new ImageObjectAdapter();
		ImageObjectAdapter adapter2 = new ImageObjectAdapter();
		try {
			adapter.load(new File("data/test_3.jpg"));
			adapter2.load(new File("data/test_3.jpg"));
			RGBHistogramExtractor extractor = new RGBHistogramExtractor();
			RGBHistogramExtractor extractor2 = new RGBHistogramExtractor();
			RGBHistogramDescriptor descriptor = (RGBHistogramDescriptor) extractor
					.extract(adapter);
			RGBHistogramDescriptor descriptor2 = (RGBHistogramDescriptor) extractor2
					.extract(adapter2);
			Assert.assertEquals(descriptor.toString(), descriptor2.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
