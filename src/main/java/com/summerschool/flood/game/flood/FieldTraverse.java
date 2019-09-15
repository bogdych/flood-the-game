package com.summerschool.flood.game.flood;

import java.util.HashSet;
import java.util.Set;

public class FieldTraverse {

    private Field field;
    private Color color;
    private FieldTraverseCallback callback;
    private Set<Cell> visited = new HashSet<>();

    public FieldTraverse(Field field) {
        this.field = field;
    }

    public void traverse(int x, int y, FieldTraverseCallback onCell) {
        Cell cell = field.getCells()[x][y];
        color = cell.getColor();
        callback = onCell;
        visited.clear();
        visited.add(cell);

        // skip origin cell

        next(x + 1, y);
        next(x - 1, y);
        next(x, y + 1);
        next(x, y - 1);
    }

    private void next(int x, int y) {
        if (!field.isInternalAt(x, y) ||
             field.getCells()[x][y].getColor() != color ||
             visited.contains(field.getCells()[x][y])) {
            return;
        }

        final Cell cell = field.getCells()[x][y];
        callback.accept(cell);
        visited.add(cell);

        next(x + 1, y);
        next(x - 1, y);
        next(x, y + 1);
        next(x, y - 1);
    }

}
