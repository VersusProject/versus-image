/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package edu.illinois.ncsa.versus.extract.impl;

import java.io.File;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.PixelHistogramDescriptor;

/**
 *
 * @author antoinev
 */
public class PixelHistogramExtractorTest {

    public PixelHistogramExtractorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void extract(String image) throws Exception {
        extract(image, true);
    }

    private void extract(String image, boolean assertArrayEquals) throws Exception {
        File file = new File(image);

        ImageObjectAdapter imageObjectAdapter = new ImageObjectAdapter();
        imageObjectAdapter.load(file);
        PixelHistogramExtractor extractor1 = new PixelHistogramExtractor();
        PixelHistogramDescriptor descriptor1 =
                (PixelHistogramDescriptor) extractor1.extract(imageObjectAdapter);
        assertNotNull(descriptor1);

        BufferedImageAdapter bufferedImageAdapter = new BufferedImageAdapter();
        bufferedImageAdapter.load(file);
        PixelHistogramExtractor extractor2 = new PixelHistogramExtractor();
        PixelHistogramDescriptor descriptor2 =
                (PixelHistogramDescriptor) extractor2.extract(bufferedImageAdapter);
        assertNotNull(descriptor2);

        if (assertArrayEquals) {
            assertArrayEquals(descriptor1.getHistogram(), descriptor2.getHistogram());
        }
    }

    @Test
    public void testExtractGrayscale8BitTif() throws Exception {
        extract("data/grayscale8bit.tif");
    }

    @Test
    public void testExtractGrayscale16BitTif() throws Exception {
        extract("data/grayscale16bit.tif");
    }

    @Test
    public void testExtractGrayscale32BitTif() throws Exception {
        try {
            extract("data/grayscale32bit.tif");
            assertFalse("Should have thrown a ArrayIndexOutOfBoundsException", true);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testExtractGrayscale8BitJpg() throws Exception {
        extract("data/grayscale8bit.jpg");
    }

    @Test
    public void testExtractGrayscale8BitPng() throws Exception {
        extract("data/grayscale8bit.png");
    }

    @Test
    public void testExtractGrayscale16BitPng() throws Exception {
        extract("data/grayscale16bit.png");
    }

    @Test
    public void testExtractColor8BitPng() throws Exception {
        extract("data/color8bit.png", false);
    }

    @Test
    public void testExtractColor24BitPng() throws Exception {
        extract("data/color24bit.png");
    }
}
