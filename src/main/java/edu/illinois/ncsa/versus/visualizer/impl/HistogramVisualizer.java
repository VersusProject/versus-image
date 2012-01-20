package edu.illinois.ncsa.versus.visualizer.impl;

import java.awt.image.BufferedImage;

import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;

import edu.illinois.ncsa.versus.adapter.Adapter;

import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.visualizer.Visualizer;

import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;

public class HistogramVisualizer implements Visualizer {


	@Override
	public BufferedImage getImage2dPreview(Extractor _extractor, Adapter _adapter) {
		
		BufferedImage prv = null;	
		
		
		if( _extractor.getName().contains("RGB") ){
			
			
			RGBHistogramDescriptor hist = null;
			try {
				hist = (RGBHistogramDescriptor)_extractor.extract(_adapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int valuesLengthR = 0;
			int valuesLengthG = 0;
			int valuesLengthB = 0;
			
			for(int i=0; i<hist.getNumBins(); i++){
				valuesLengthR += hist.get(i,0);
				valuesLengthG += hist.get(i,1);
				valuesLengthB += hist.get(i,2);
			}
			
			double valuesR[] = new double[valuesLengthR];
			double valuesG[] = new double[valuesLengthG];
			double valuesB[] = new double[valuesLengthB];
						
			for( int d=0; d<3; d++){
				
				int index = 0;
				
				for( int i=0; i < hist.getNumBins(); i++){
					
					int binCount = hist.get(i,d);
					
					for( int j=0; j < binCount; j++){
						
						if(d==0){
							valuesR[index] = i;
						}
						else if(d==1){
							valuesG[index] = i;
						}
						else{
							valuesB[index] = i;
						}						
						index++;
					}			
				}
			}
			
			HistogramDataset dsetR = new HistogramDataset(); 
			dsetR.setType(HistogramType.FREQUENCY);		
			dsetR.addSeries("Red Pixel Count", valuesR, valuesR.length);
			
			HistogramDataset dsetG = new HistogramDataset(); 
			dsetG.setType(HistogramType.FREQUENCY);		
			dsetG.addSeries("Green Pixel Count", valuesG, valuesG.length);
			
			HistogramDataset dsetB = new HistogramDataset(); 
			dsetB.setType(HistogramType.FREQUENCY);		
			dsetB.addSeries("Blue Pixel Count", valuesB, valuesB.length);
			
			JFreeChart hChartR = ChartFactory.createHistogram( null, null,"Count",
								dsetR,PlotOrientation.VERTICAL,false,false,false);	
			
			JFreeChart hChartG = ChartFactory.createHistogram( null, null,"Count",
					dsetG,PlotOrientation.VERTICAL,false,false,false);
			
			JFreeChart hChartB = ChartFactory.createHistogram( null, null,"Count",
					dsetB,PlotOrientation.VERTICAL,false,false,false);
			
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Pixel Intensity"));
			
			plot.add(hChartR.getXYPlot(),1);
			plot.add(hChartB.getXYPlot(),1);
			plot.add(hChartG.getXYPlot(),1);
			plot.setOrientation(PlotOrientation.VERTICAL);
			
			JFreeChart a = new JFreeChart("Color Histogram",JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			
			prv = a.createBufferedImage(800, 800);
			
		}
		else if (_extractor.getName().contains("Grayscale") ){
			
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
			
			double[] values = new double[valuesLength];
			int index       = 0;
			
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
			
			JFreeChart hChart = ChartFactory.createHistogram( "Grayscale Histogram", "Pixel Intensity","Count",
								dset,PlotOrientation.VERTICAL,false,false,false);			
			
			prv = hChart.createBufferedImage(800, 800);
			
		}
			return prv;	
	}

	@Override
	public String getName() {
		return "Histogram Visualizer";
	}	
}
 