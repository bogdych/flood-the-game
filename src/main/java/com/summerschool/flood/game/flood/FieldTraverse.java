package com.summerschool.flood.game.flood;

public class FieldTraverse {

    private Field field;
    private Color color;
    private FieldTraverseCallback callback;

    public FieldTraverse(Field field) {
        this.field = field;
    }

    public void traverse(int x, int y, Color color, FieldTraverseCallback onCell) {
        color = field.getCells()[x][y].getColor();
        callback = onCell;
        next(x, y);
    }

    private void next(int x, int y) {
        if (!field.isInternalAt(x, y) || field.getCells()[x][y].getColor() != color) {
            return;
        }

        final Cell cell = field.getCells()[x][y];
        callback.accept(cell);

        next(x - 1, y);
        next(x + 1, y);
        next(x, y - 1);
        next(x, y + 1);
    }

}
