package com.summerschool.flood.game.flood;

public class FieldTraverse {

    private Field field;
    private Color color;
    private FieldTraverseCallback callback;

    public FieldTraverse(Field field) {
        this.field = field;
    }

    public void traverse(int x, int y, FieldTraverseCallback onCell) {
        color = field.getCells()[x][y].getColor();
        callback = onCell;

        int left = -1, right = 1, top = 1, bottom = -1;

        next(x, y + top, right, top);
        next(x + right, y, right, bottom);
        next(x, y + bottom, left, bottom);
        next(x + left, y, left, top);
    }

    private void next(int x, int y, int advanceX, int advanceY) {
        if (!field.isInternalAt(x, y) || field.getCells()[x][y].getColor() != color) {
            return;
        }

        final Cell cell = field.getCells()[x][y];
        callback.accept(cell);

        next(x + advanceX, y, advanceX, advanceY);
        next(x, y + advanceY, advanceX, advanceY);
    }

}
