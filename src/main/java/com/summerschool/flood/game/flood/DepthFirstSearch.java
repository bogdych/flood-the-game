package com.summerschool.flood.game.flood;

import com.summerschool.flood.game.IFirstSearch;

import java.util.List;
import java.util.ArrayList;

public class DepthFirstSearch implements IFirstSearch {

	private Color startColor;
	private Color fillColor;
	private Field field;

	public DepthFirstSearch(Field field) {
		this.field = field;
	}

	public List<Cell> start(int x, int y, Color color, List<Cell> otherPlayers) {
		List<Cell> changeList = new ArrayList<>();
		startColor = field.getCells()[x][y].getColor();
		fillColor = color;
		runNext(x, y);
		boolean[][] used = new boolean[field.getWidth()][field.getWidth()];
		runVoidNext(x, y, otherPlayers, changeList, used);
		return changeList;
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

	/**
	 * TODO Комментарий
	 *
	 * @param x
	 * @param y
	 * @param otherPlayers
	 * @param changeList
	 * @param used
	 */
	private void runVoidNext(int x, int y, List<Cell> otherPlayers, List<Cell> changeList, boolean[][] used) {
		if (!field.isInternalAt(x, y)
				|| field.getCells()[x][y].getColor() != fillColor
				|| used[x][y]) {
			return;
		}
		used[x][y] = true;
		final Cell cell = field.getCells()[x][y];
		if (otherPlayers.contains(cell)) {
			changeList.add(cell);
		}

		runVoidNext(x - 1, y, otherPlayers, changeList, used);
		runVoidNext(x + 1, y, otherPlayers, changeList, used);
		runVoidNext(x, y - 1, otherPlayers, changeList, used);
		runVoidNext(x, y + 1, otherPlayers, changeList, used);
	}
}
