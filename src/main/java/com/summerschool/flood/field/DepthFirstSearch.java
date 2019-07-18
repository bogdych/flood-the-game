package com.summerschool.flood.field;

public class DepthFirstSearch {
    private Array2D<Integer> matrix;
    private int startValue;
    public DepthFirstSearch(Array2D<Integer> square){
        this.matrix = square;
    }
    /* O(|V| + |E|) */
    public void start(Pair<Integer, Integer> cell, int value){
        startValue = (int) matrix.getAt(cell.getFirst(), cell.getSecond());
        runNext(cell.getFirst(), cell.getSecond(), value);
    }
    public void start(int xCell, int yCell, int value){
        startValue = (int) matrix.getAt(xCell, yCell);
        runNext(xCell, yCell, value);
    }
    private void runNext(int xCell, int yCell, int value){
        /*"used" is excess. Think, that startValue is enough*/
        if(!matrix.isInternalCell(xCell, yCell) || matrix.getAt(xCell, yCell) != startValue)
            return;
        matrix.setAt(xCell, yCell, value);

        if(matrix.isInternalCell(xCell - 1, yCell) && matrix.getAt(xCell - 1, yCell) == startValue)
            runNext(xCell - 1, yCell, value);
        if(matrix.isInternalCell(xCell, yCell + 1) && matrix.getAt(xCell, yCell + 1) == startValue)
            runNext(xCell, yCell + 1, value);
        if(matrix.isInternalCell(xCell + 1, yCell) && matrix.getAt(xCell + 1, yCell) == startValue)
            runNext(xCell + 1, yCell, value);
        if(matrix.isInternalCell(xCell, yCell - 1) && matrix.getAt(xCell, yCell - 1) == startValue)
            runNext(xCell, yCell - 1, value);
    }
}
