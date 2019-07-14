package com.summerschool.flood.gameSquare;

import java.util.Random;
import lombok.Getter;


public class Square implements Array2D<Integer>{
    private static final @Getter int xDef = 16;
    private static final @Getter int yDef = 16;
    private final @Getter int xSize;
    private final @Getter int ySize;

    private final @Getter int colorNumberDef = 6;
    private final @Getter int colorNumber;

    private Matrix2D<Integer> matrix;
    private @Getter String status = "";
    public Square(){
        this.xSize = xDef;
        this.ySize = yDef;
        this.colorNumber = colorNumberDef;
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        this.matrix = new Matrix2D<Integer>(xSize, ySize, 0);
    }
    public Square(int xs, int ys, int cs){

        this.xSize = xs;
        this.ySize = ys;
        this.colorNumber = cs;
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        this.matrix = new Matrix2D<Integer>(xSize, ySize, 0);
        for (int j = 0; j < xSize; j++) {
            for (int i = 0; i < ySize; i++)
                matrix.setAt(i, j, rand.nextInt(colorNumber));
        }
    }
    public Boolean IsInternalCell(int xCell, int yCell){
        if(xCell < 0 || xSize <= xCell ||
                yCell < 0 || ySize <= yCell)
            return false;
        return true;
    }
    public int getColorAt(int xc, int yc){
        return (int) matrix.getAt(xc, yc);
    }
    public int getColorAt(Pair<Integer, Integer> cell){
        return (int) matrix.getAt(cell);
    }
    public void setColorAt(int xc, int yc, int valueColor){
        matrix.setAt(xc, yc, valueColor);
    }
    public void setColorAt(Pair<Integer, Integer> cell, int valueColor){
        matrix.setAt(cell, valueColor);
    }
    public Integer getAt(int x, int y){
        return this.getColorAt(x, y);
    }
    public void setAt(int x, int y, Integer valueColor){
        this.setColorAt(x, y, valueColor);
    }
}
