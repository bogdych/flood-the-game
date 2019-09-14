package com.summerschool.flood.game.flood;

@FunctionalInterface
public interface FieldTraverseCallback {
    void accept(Cell cell);
}
