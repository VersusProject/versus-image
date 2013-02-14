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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAndMaskAdapter;
import edu.illinois.ncsa.versus.descriptor.impl.EntropyDescriptor;
import ncsa.im2learn.core.datatype.ImageException;
import ncsa.im2learn.core.datatype.ImageObject;

/**
 *
 * @author antoinev
 */
public class EntropyExtractorTest {

    public EntropyExtractorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testImEntropyImageObject() throws Exception {
        ImageObject img1 = createBlankImage();
        ImageObject img2 = createCheckerImage();

        ImageObjectAndMaskAdapter adapter1 =
                new ImageObjectAndMaskAdapter(img1, null);

        ImageObjectAndMaskAdapter adapter2 =
                new ImageObjectAndMaskAdapter(img2, null);

        EntropyExtractor entropyExtractor = new EntropyExtractor();

        EntropyDescriptor entropy1 =
                (EntropyDescriptor) entropyExtractor.extract(adapter1);
        EntropyDescriptor entropy2 =
                (EntropyDescriptor) entropyExtractor.extract(adapter2);
        double score = entropy1.getEntropy();
        double score2 = entropy2.getEntropy();
        System.out.println("Test Score = " + score);
        System.out.println("Test Score2 = " + score2);
        assertEquals(score, 0, 0.00001);
        assertEquals(score2, 12.5, 0.00001);
    }

    public ImageObject createBlankImage() throws ImageException {
        ImageObject im = ImageObject.createImage(100, 100, 3, ImageObject.TYPE_BYTE);

        im.setData(255);
        return im;
    }

    public ImageObject createCheckerImage() throws ImageException {
        ImageObject im = ImageObject.createImage(100, 100, 1, ImageObject.TYPE_BYTE);

        im.setData(255);

        int offset = 0;
        for (int row = 0; row < im.getNumRows(); row++) {
            if (((row >> 1) << 1) == row) {
                // even
                offset = 0;
            } else {
                offset = 1;
            }
            for (int col = 0; col < im.getNumCols(); col++) {
                if ((((col + offset) >> 1) << 1) == (col + offset)) {
                    // even
                    im.set(row, col, 0, 0);
                }
            }
        }
        return im;
    }
}
