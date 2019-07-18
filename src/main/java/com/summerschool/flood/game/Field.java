package com.summerschool.flood.game;

import lombok.Data;

@Data
public class Field {

    private Cell[][] cells;

    public Field(int width, int height) {
        this.cells = new Cell[width][height];
        fill(cells);
    }

    private void fill(Cell[][] cells) {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

}