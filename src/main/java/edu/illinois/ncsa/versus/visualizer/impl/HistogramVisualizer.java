package edu.illinois.ncsa.versus.visualizer.impl;

import java.awt.image.BufferedImage;

import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;

import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.visualizer.Visualizer;

import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;

public class HistogramVisualizer implements Visualizer {


	@Override
	public BufferedImage getImage2dPreview(Extractor _extractor, Adapter _adapter) {
		
		BufferedImage prv                 = ((BufferedImageAdapter) _adapter).getImage();		
		GrayscaleHistogramDescriptor hist = null;
		try {
			hist = (GrayscaleHistogramDescriptor)_extractor.extract(_adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int valuesLength = 0;
		for(int i=0; i<hist.getNumBins(); i++){
			valuesLength += hist.get(i);
		}
		
		double[] values = new double[valuesLength]; //incorrect!!!
		System.out.println(valuesLength);
		int index = 0;
		for( int i=0; i < hist.getNumBins(); i++){
			
			int binCount = hist.get(i);
			
			for( int j=0; j < binCount; j++){
				values[index] = i;
				index++;
			}			
		}
		
		HistogramDataset dset = new HistogramDataset(); 
		dset.setType(HistogramType.FREQUENCY);		
		dset.addSeries("GrayscaleHistogram", values, values.length);
		
		JFreeChart hChart = ChartFactory.createHistogram( "Histogram Visualization", "Pixel Intensity","Count",
							dset,PlotOrientation.VERTICAL,false,false,false);
		
		
		prv = hChart.createBufferedImage(800, 800);
		
		return prv;	
	}

	@Override
	public String getName() {
		return "Histogram Visualizer";
	}	
}
 