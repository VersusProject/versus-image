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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import org.junit.Ignore;

/**
 *
 * @author antoinev
 */
public class GrayscaleHistogramExtractorTest {

    public GrayscaleHistogramExtractorTest() {
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
        File file = new File(image);

        ImageObjectAdapter imageObjectAdapter = new ImageObjectAdapter();
        imageObjectAdapter.load(file);
        GrayscaleHistogramExtractor extractor1 = new GrayscaleHistogramExtractor();
        GrayscaleHistogramDescriptor descriptor1 =
                (GrayscaleHistogramDescriptor) extractor1.extract(imageObjectAdapter);
        assertNotNull(descriptor1);

        BufferedImageAdapter bufferedImageAdapter = new BufferedImageAdapter();
        bufferedImageAdapter.load(file);
        GrayscaleHistogramExtractor extractor2 = new GrayscaleHistogramExtractor();
        GrayscaleHistogramDescriptor descriptor2 =
                (GrayscaleHistogramDescriptor) extractor2.extract(bufferedImageAdapter);
        assertNotNull(descriptor2);

        assertArrayEquals(descriptor1.getHistogram(), descriptor2.getHistogram());
    }

    @Test
    public void testExtract8BitTif() throws Exception {
        extract("data/grayscale8bit.tif");
    }

    @Ignore("16bpp not supported yet.")
    @Test
    public void testExtract16BitTif() throws Exception {
        extract("data/grayscale16bit.tif");
    }

    @Test
    public void testExtract32BitTif() throws Exception {
        try {
            extract("data/grayscale32bit.tif");
            assertFalse("Should have thrown a UnsupportedOperationException", true);
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }

    @Ignore("No loader for grayscale jpg on linux.")
    @Test
    public void testExtract8BitJpg() throws Exception {
        extract("data/grayscale8bit.jpg");
    }

    @Test
    public void testExtract8BitPng() throws Exception {
        extract("data/grayscale8bit.png");
    }

    @Ignore("16bpp not supported yet.")
    @Test
    public void testExtract16BitPng() throws Exception {
        extract("data/grayscale16bit.png");
    }
}
