package com.summerschool.flood.game.flood;

import com.summerschool.flood.game.IFirstSearch;

public class DepthFirstSearch implements IFirstSearch {

	private Color startColor;
	private Color fillColor;
	private Field field;

	public DepthFirstSearch(Field field) {
		this.field = field;
	}

	public void start(int x, int y, Color color) {
		startColor = field.getCells()[x][y].getColor();
		fillColor = color;
		runNext(x, y);
	}

	/**
	 * Recursive fill of the game field with chosen color from specified point
	 *
	 * @param x X coordinate of the cell to be filled
	 * @param y Y coordinate of the cell to be filled
	 */
	private void runNext(int x, int y) {
		if (!field.isInternalAt(x, y) || field.getCells()[x][y].getColor() != startColor) {
			return;
		}

		final Cell cell = field.getCells()[x][y];
		cell.setColor(fillColor);

		runNext(x - 1, y);
		runNext(x + 1, y);
		runNext(x, y - 1);
		runNext(x, y + 1);
	}

}
