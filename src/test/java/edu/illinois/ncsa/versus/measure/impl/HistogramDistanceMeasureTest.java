package edu.illinois.ncsa.versus.measure.impl;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;
import edu.illinois.ncsa.versus.extract.impl.PixelHistogramExtractor;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;

public class HistogramDistanceMeasureTest {

	@Test
	public void testRepeatMeasure() {
		ImageObjectAdapter adapter = new ImageObjectAdapter();
		ImageObjectAdapter adapter2 = new ImageObjectAdapter();
		try {
			adapter.load(new File("data/test_4.jpg"));
			adapter2.load(new File("data/test_4.jpg"));
			PixelHistogramExtractor extractor = new PixelHistogramExtractor();
			PixelHistogramExtractor extractor2 = new PixelHistogramExtractor();
			PixelHistogramDescriptor descriptor = (PixelHistogramDescriptor) extractor
					.extract(adapter);
			PixelHistogramDescriptor descriptor2 = (PixelHistogramDescriptor) extractor2
					.extract(adapter2);

			HistogramDistanceMeasure measure = new HistogramDistanceMeasure();
			SimilarityNumber similarity = measure.compare(descriptor,
					descriptor2);
			HistogramDistanceMeasure measure2 = new HistogramDistanceMeasure();
			SimilarityNumber similarity2 = measure2.compare(descriptor,
					descriptor2);
			System.out.println("Similarity 1 = " + similarity.getValue());
			System.out.println("Similarity 2 = " + similarity2.getValue());
			Assert.assertEquals(similarity.getValue(), similarity2.getValue(),
					0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
