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
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.ColorLayoutFeature;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;

/**
 * Based on
 *
 * http://www.lac.inpe.br/JIPCookbook/6050-howto-compareimages.jsp
 *
 * @author Luigi Marini
 *
 */
public class ColorAverageVectorFeatureExtractor implements Extractor, HasCategory {

    private ColorLayoutFeature calcSignature(double[][][] data) {
        ColorLayoutFeature signatureVector = new ColorLayoutFeature(
                data.length, data[0].length);

        // Do a 9 x 9 grid of points
        double[] propotions = new double[]{1 / 10, 2 / 10, 3 / 10, 4 / 10,
            5 / 10, 6 / 10, 7 / 10, 8 / 10, 9 / 10};
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Color color = boxAverage(data, propotions[x], propotions[y]);
                signatureVector.setSignature(color, x, y);
            }
        }
        return signatureVector;
    }

    /**
     * TODO implement!
     *
     * @param data
     * @param x
     * @param y
     * @return
     */
    private Color boxAverage(double[][][] data, double x, double y) {
        return null;
    }

    @Override
    public BufferedImageAdapter newAdapter() {
        return new BufferedImageAdapter();
    }

    @Override
    public String getName() {
        return "Pixels to Vector";
    }

    @Override
    public Descriptor extract(Adapter adapter) throws Exception {
        if (adapter instanceof HasPixels) {
            HasPixels hasPixels = (HasPixels) adapter;
            return calcSignature(hasPixels.getRGBPixels());
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
