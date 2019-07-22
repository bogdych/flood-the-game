package com.summerschool.flood.game;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DepthFirstSearch implements IFirstSearch {
    private Color startColor;
    private Field field;
    public DepthFirstSearch(Field field){
        this.field = field;
    }
    /* O(|V| + |E|) */
    public void start(Cell cell){
        startColor = field.getCells()[cell.getX()][cell.getY()].getColor();
        runNext(cell);
    }

    private void runNext(Cell cell){
        /*"used" is excess. Think, that startColor is enough*/
        if(!field.isInternalAt(cell.getX(), cell.getY()) ||
                field.getCells()[cell.getX()][cell.getY()].getColor() != startColor)
            return;
        field.getCells()[cell.getX()][cell.getY()].setColor(cell.getColor());


        if(checkLeft(cell))
            runNext(field.getCells()[cell.getX() - 1][cell.getY()]);

        if(checkUp(cell))
            runNext(field.getCells()[cell.getX()][cell.getY() + 1]);

        if(checkRight(cell))
            runNext(field.getCells()[cell.getX() + 1][cell.getY()]);

        if(checkDown(cell))
            runNext(field.getCells()[cell.getX()][cell.getY() - 1]);
    }


    private Boolean checkLeft(Cell cell){
        return field.isInternalAt(cell.getX() - 1, cell.getY()) &&
                field.getCells()[cell.getX() - 1][cell.getY()].getColor() == startColor;
    }
    private Boolean checkRight(Cell cell){
        return field.isInternalAt(cell.getX() + 1, cell.getY()) &&
                field.getCells()[cell.getX() + 1][cell.getY()].getColor() == startColor;
    }
    private Boolean checkUp(Cell cell){
        return field.isInternalAt(cell.getX(), cell.getY() + 1) &&
                field.getCells()[cell.getX()][cell.getY() + 1].getColor() == startColor;
    }
    private Boolean checkDown(Cell cell){
        return field.isInternalAt(cell.getX(), cell.getY() - 1) &&
                field.getCells()[cell.getX()][cell.getY() - 1].getColor() == startColor;
    }
}