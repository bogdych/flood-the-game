package com.summerschool.flood.game;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class Field {

    private Cell[][] cells;
    private final int width;
    private final int height;
    private List<Cell> startPositions;
    private int counter = 0;
    private IFirstSearch firstSearch;
    public Field(int width, int height) {
        this.cells = new Cell[width][height];
        this.width = width;
        this.height = height;
        fill(cells);
        firstSearch = new DepthFirstSearch();
    }

    private void fill(Cell[][] cells) {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }
    public void makeStep(int x, int y, Color color){
        if(!isValidMakeStep(x, y, color))
            return;
        Cell tmpCell = new Cell(x, y);
        tmpCell.setColor(color);
        firstSearch.start(tmpCell);
        counter = (counter + 1) % startPositions.size();
    }
    public Boolean isValidMakeStep(int x, int y, Color color){
        Cell tmpCell = startPositions.get(counter);
        return tmpCell.getX() == x && tmpCell.getY() == y && tmpCell.getColor() != color && isInternalAt(x, y);
    }
    public Boolean isInternalAt(int x, int y){
        return 0 < x && x < cells.length &&
               0 < y && y < cells[x].length;
    }
    public interface IFirstSearch {
        void start(Cell cell);
    }
    @NoArgsConstructor
    private class DepthFirstSearch implements IFirstSearch{
        private Cell[][] field;
        private Color startColor;
        /* O(|V| + |E|) */
        public void start(Cell cell){
            startColor = getCells()[cell.getX()][cell.getY()].getColor();
            runNext(cell);
        }

        private void runNext(Cell cell){
            /*"used" is excess. Think, that startColor is enough*/
            if(!isInternalAt(cell.getX(), cell.getY()) ||
                    getCells()[cell.getX()][cell.getY()].getColor() != startColor)
                return;
            getCells()[cell.getX()][cell.getY()].setColor(cell.getColor());


            if(isInternalAt(cell.getX() - 1, cell.getY()) &&
                    getCells()[cell.getX() - 1][cell.getY()].getColor() == startColor)
                runNext(getCells()[cell.getX() - 1][cell.getY()]);

            if(isInternalAt(cell.getX(), cell.getY() + 1) &&
                    getCells()[cell.getX()][cell.getY() + 1].getColor() == startColor)
                runNext(getCells()[cell.getX()][cell.getY() + 1]);

            if(isInternalAt(cell.getX() + 1, cell.getY()) &&
                    getCells()[cell.getX() + 1][cell.getY()].getColor() == startColor)
                runNext(getCells()[cell.getX() + 1][cell.getY()]);

            if(isInternalAt(cell.getX(), cell.getY() - 1) &&
                    getCells()[cell.getX()][cell.getY() - 1].getColor() == startColor)
                runNext(getCells()[cell.getX()][cell.getY() - 1]);
        }
    }
}