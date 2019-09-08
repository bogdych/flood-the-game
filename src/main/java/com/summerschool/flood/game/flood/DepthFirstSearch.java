package com.summerschool.flood.game.flood;

import com.summerschool.flood.game.IFirstSearch;

import java.util.List;
import java.util.ArrayList;

public class DepthFirstSearch implements IFirstSearch {

    private Color startColor;
    private Color fillColor;
    private Field field;

    public DepthFirstSearch(Field field){
        this.field = field;
    }

    public List<Cell> start(int x, int y, Color color, List<Cell> otherPlayers) {
        List<Cell> changeList= new ArrayList<>();
        startColor = field.getCells()[x][y].getColor();
        fillColor = color;
        runNext(x, y, otherPlayers, changeList);
        return changeList;
    }

    /**
     * Recursive fill of the game field with chosen color from specified point
     * @param x X coordinate of the cell to be filled
     * @param y Y coordinate of the cell to be filled
     */
    private void runNext(int x, int y, List<Cell> otherPlayers, List<Cell> changeList) {
        if (!field.isInternalAt(x, y) ||
            field.getCells()[x][y].getColor() != startColor)
            return;

        if (otherPlayers.contains(field.getCells()[x][y]))
            changeList.add(field.getCells()[x][y]);

        field.getCells()[x][y].setColor(fillColor);

        runNext(x - 1, y, otherPlayers, changeList);
        runNext(x + 1, y, otherPlayers, changeList);
        runNext(x, y - 1, otherPlayers, changeList);
        runNext(x, y + 1, otherPlayers, changeList);
    }

}