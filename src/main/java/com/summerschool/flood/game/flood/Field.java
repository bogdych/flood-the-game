package com.summerschool.flood.game.flood;

import lombok.Data;

@Data
public class Field {

    private Cell[][] cells;
    private final int width;
    private final int height;

    public Field(int width, int height) {
        this.cells = new Cell[width][height];
        this.width = width;
        this.height = height;
        fill(cells);
    }

    private void fill(Cell[][] cells) {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public Boolean isInternalAt(int x, int y){
        return 0 <= x && x < cells.length &&
               0 <= y && y < cells[x].length;
    }
}