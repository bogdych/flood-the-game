package com.summerschool.flood.gameSquare;

public class DFS {
    private Array2D<Integer> square;
    private int startValue;
    public DFS(Array2D<Integer> sqr){
        this.square = sqr;
    }
    /* O(|V| + |E|) */
    public void Start(Pair<Integer, Integer> cell, int value){
        startValue = (int) square.getAt(cell.getFirst(), cell.getSecond());
        Run(cell.getFirst(), cell.getSecond(), value);
    }
    private void Run(int xCell, int yCell, int value){
        /*"used" is excess. Think, that startValue is enough*/
        if(!square.IsInternalCell(xCell, yCell) || square.getAt(xCell, yCell) != startValue)
            return;
        square.setAt(xCell, yCell, value);

        Run(xCell - 1, yCell, value);
        Run(xCell, yCell + 1, value);
        Run(xCell + 1, yCell, value);
        Run(xCell, yCell - 1, value);
    }
}
