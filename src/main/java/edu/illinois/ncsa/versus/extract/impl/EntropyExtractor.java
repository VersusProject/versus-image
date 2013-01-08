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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAndMaskAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.EntropyDescriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;
import ncsa.im2learn.core.datatype.ImageObject;
import ncsa.im2learn.core.datatype.LimitValues;
import ncsa.im2learn.ext.statistics.Histogram;

/**
 *
 * @author antoinev
 */
public class EntropyExtractor implements Extractor, HasCategory {

    @Override
    public Descriptor extract(Adapter adapter) throws Exception {
        if (!(adapter instanceof ImageObjectAdapter)) {
            throw new UnsupportedTypeException("Invalid adapter type.");
        }

        ImageObjectAdapter imageAdapter = (ImageObjectAdapter) adapter;

        ImageObject image = imageAdapter.getImageObject();
        ImageObject mask = null;
        byte maskVal = -1;

        if(adapter instanceof ImageObjectAndMaskAdapter) {
            mask = ((ImageObjectAndMaskAdapter)imageAdapter).getMaskImageObject();
        }
        
        // arrays with entropy score per band
        double[] scoreBands = new double[image.getNumBands()];

        Histogram hist = new Histogram();

        // initialize histogram bins depending on the data type
        if (image.getTypeString().equalsIgnoreCase("BYTE")) {
            hist.SetIs256Bins(true);
        }
        if (image.getTypeString().equalsIgnoreCase("SHORT")
                || image.getTypeString().equalsIgnoreCase("USHORT")) {
            hist.SetMinDataVal(0.0);
            hist.SetMaxDataVal((double) (LimitValues.MAXPOS_SHORT - 1));// 65535.0;
            if (hist.GetNumBins() != LimitValues.MAXPOS_SHORT) {
                // memory allocation only if necessary
                hist.SetNumBins(LimitValues.MAXPOS_SHORT);// default number of bins
                // is 256
            } else {
                hist.SetWideBins(1.0);
            }
            hist.SetIs256Bins(false);
        }
        if (image.getTypeString().equalsIgnoreCase("INT")
                || image.getTypeString().equalsIgnoreCase("LONG")
                || image.getTypeString().equalsIgnoreCase("FLOAT")
                || image.getTypeString().equalsIgnoreCase("DOUBLE")) {
            hist.SetIs256Bins(false);

            image.computeMinMax();
            double absMin = image.getMin();
            double absMax = image.getMax();

            hist.SetMinDataVal(absMin);
            hist.SetMaxDataVal(absMax);

            if ((int) (absMax - absMin + 0.5) > LimitValues.MAXPOS_SHORT) {
                hist.SetNumBins((int) (LimitValues.MAXPOS_SHORT));
            } else {
                if (absMax - absMin < 256) {
                    hist.SetNumBins(256);
                } else {
                    hist.SetNumBins((int) (absMax - absMin + 0.5));
                }
            }
        }
        // end initialize histogram bins depending on the data type


        // compute entropy measure for each band
        int band;
        double maxlog = -Math.log(1.0 / (double) hist.GetNumBins());
        double rangelog = maxlog / 100.0; // maxlog - (minlog=0)

        for (band = 0; band < image.getNumBands(); band++) {
            if (mask == null) {
                hist.Hist(image, band);
            } else {
                hist.HistMask(image, band, mask, maskVal);
            }
            hist.Entropy();
            scoreBands[band] = 100.0 - (maxlog - hist.GetEntropy())
                    / rangelog;

            // test
            System.out.println("Test: Entropy[" + band + "]="
                    + hist.GetEntropy());
            System.out.println("Test: Normalized Entropy[" + band + "]="
                    + scoreBands[band]);
        }

        double result = 0.0;
        for (band = 0; band < image.getNumBands(); band++) {
            result += scoreBands[band];
        }
        result = result / image.getNumBands();
        return new EntropyDescriptor(result);
    }

    @Override
    public Adapter newAdapter() {
        return new ImageObjectAndMaskAdapter();
    }

    @Override
    public String getName() {
        return "Entropy";
    }

    @Override
    public Set<Class<? extends Adapter>> supportedAdapters() {
        return new HashSet<Class<? extends Adapter>>(Arrays.asList(
                ImageObjectAdapter.class,
                ImageObjectAndMaskAdapter.class));
    }

    @Override
    public Class<? extends Descriptor> getFeatureType() {
        return EntropyDescriptor.class;
    }

    @Override
    public boolean hasPreview() {
        return false;
    }

    @Override
    public String previewName() {
        return null;
    }

    @Override
    public String getCategory() {
        return "2D";
    }
}
