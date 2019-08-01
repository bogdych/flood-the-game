package com.summerschool.flood.game.flood;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /** @return True, whether the field is filled only by single color */
    @JsonIgnore
    public Boolean isFilledByOneColor() {
        Color color = cells[0][0].getColor();
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (cells[x][y].getColor() != color) return false;
            }
        }

        return true;
    }


    @JsonIgnore
    public Boolean isInternalAt(int x, int y){
        return 0 <= x && x < cells.length &&
               0 <= y && y < cells[x].length;
    }

    /** @return Pretty printed quad field [for debug only] */
    @JsonIgnore
    public String getPrettyView() {
        StringBuilder builder = new StringBuilder();

        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                builder.append(cells[x][y].getColor().toString().charAt(0));
            }
            builder.append('\n');
        }

        return builder.toString();
    }

}