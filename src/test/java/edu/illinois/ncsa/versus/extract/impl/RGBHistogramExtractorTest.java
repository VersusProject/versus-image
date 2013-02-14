package edu.illinois.ncsa.versus.extract.impl;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
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
            RGBHistogramDescriptor descriptor = (RGBHistogramDescriptor) extractor.extract(adapter);
            RGBHistogramDescriptor descriptor2 = (RGBHistogramDescriptor) extractor2.extract(adapter2);
            assertEquals(descriptor.toString(), descriptor2.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testExtractColor8BitPng() throws Exception {
        try {
            extract("data/color8bit.png");
        } catch (UnsupportedTypeException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testExtractColor24BitPng() throws Exception {
        extract("data/color24bit.png");
    }

    private void extract(String image) throws Exception {
        File file = new File(image);

        ImageObjectAdapter imageObjectAdapter = new ImageObjectAdapter();
        imageObjectAdapter.load(file);
        RGBHistogramExtractor extractor1 = new RGBHistogramExtractor();
        RGBHistogramDescriptor descriptor1 =
                (RGBHistogramDescriptor) extractor1.extract(imageObjectAdapter);
        assertNotNull(descriptor1);

        BufferedImageAdapter bufferedImageAdapter = new BufferedImageAdapter();
        bufferedImageAdapter.load(file);
        RGBHistogramExtractor extractor2 = new RGBHistogramExtractor();
        RGBHistogramDescriptor descriptor2 =
                (RGBHistogramDescriptor) extractor2.extract(bufferedImageAdapter);
        assertNotNull(descriptor2);

        assertArrayEquals(descriptor1.getHistogram(), descriptor2.getHistogram());
    }
}
