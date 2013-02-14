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
package edu.illinois.ncsa.versus.measure.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.DoubleValueDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.EntropyDescriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;

/**
 *
 * @author antoinev
 */
public class DifferenceMeasure implements Measure {

    @Override
    public Similarity compare(Descriptor feature1, Descriptor feature2) throws Exception {

        if (!(feature1 instanceof DoubleValueDescriptor) || !(feature2 instanceof DoubleValueDescriptor)) {
            throw new UnsupportedTypeException(
                    "Difference measure expects feature of type DoubleValueFeature.");
        }
        double value1 = ((DoubleValueDescriptor) feature1).getValue();
        double value2 = ((DoubleValueDescriptor) feature2).getValue();
        return new SimilarityNumber(Math.abs(value1 - value2));
    }

    @Override
    public SimilarityPercentage normalize(Similarity similarity) {
        return null;
    }

    @Override
    public Set<Class<? extends Descriptor>> supportedFeaturesTypes() {
        return new HashSet<Class<? extends Descriptor>>(Arrays.asList(
                DoubleValueDescriptor.class,
                EntropyDescriptor.class));
    }

    @Override
    public String getName() {
        return "Difference measure";
    }

    @Override
    public Class<?> getType() {
        return DifferenceMeasure.class;
    }
}
