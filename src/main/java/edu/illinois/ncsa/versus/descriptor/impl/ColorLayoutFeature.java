/**
 * 
 */
package edu.illinois.ncsa.versus.descriptor.impl;

import java.awt.Color;

import edu.illinois.ncsa.versus.descriptor.Descriptor;

/**
 * A two dimensional grid of subimages over an image. Each subimage stores a
 * color representing pixels in that subarea.
 * 
 * @author Luigi Marini
 * 
 */
public class ColorLayoutFeature implements Descriptor {

	private final Color[][] data;

	/**
	 * Create a new empty vector of specific dimensions.
	 * 
	 * @param width
	 *            number of horizontal elements in the grid
	 * @param height
	 *            number of vertical elements in the grid
	 */
	public ColorLayoutFeature(int width, int height) {
		data = new Color[width][height];
	}

	/**
	 * Set one of the elements of the grid to a particular color.
	 * 
	 * @param color
	 *            the specific color for a specific region
	 * @param x
	 *            the x coordinate of element in the vector
	 * @param y
	 *            the y coordinate of element in the vector
	 */
	public void setSignature(Color color, int x, int y) {
		data[x][y] = color;
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	@Override
	public String getName() {
		return "Signature Vector";
	}

}
