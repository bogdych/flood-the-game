package com.summerschool.flood.field;

import java.util.Random;

import lombok.Getter;

public class GameField implements Array2D<Integer>{
    private static final @Getter int XDEF = 16;
    private static final @Getter int YDEF = 16;
    private final @Getter int xSize;
    private final @Getter int ySize;

    private final @Getter int colorNumberDef = 6;
    private final @Getter int colorNumber;

    private Matrix2D<Integer> matrix;
    private @Getter String status = "";
    public GameField(){
        this.xSize = XDEF;
        this.ySize = YDEF;
        this.colorNumber = colorNumberDef;
        this.matrix = new Matrix2D<>(xSize, ySize, 0);
        this.setRand();
    }
    public GameField(int xs, int ys, int cs){

        this.xSize = xs;
        this.ySize = ys;
        this.colorNumber = cs;
        this.matrix = new Matrix2D<>(xSize, ySize, 0);
        this.setRand();
    }
    public void setRand(){
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int j = 0; j < xSize; j++) {
            for (int i = 0; i < ySize; i++)
                matrix.setAt(j, i, rand.nextInt(colorNumber));
        }
    }
    public Boolean isInternalCell(int xCell, int yCell){
        return matrix.isInternalCell(xCell, yCell);
    }

    public Integer getAt(int x, int y){
        return matrix.getAt(x, y);
    }
    public Integer getAt(Pair<Integer, Integer>  pair){
        return getAt(pair.getFirst(), pair.getSecond());
    }
    public void setAt(int x, int y, Integer valueColor){
        matrix.setAt(x, y, valueColor);
    }
    public void setAt(Pair<Integer, Integer>  pair, Integer valueColor){
        setAt(pair.getFirst(), pair.getSecond(), valueColor);
    }
    public void show(){
        for (int i = 0; i < xSize; i++) {
            StringBuffer stringBuffer = new StringBuffer(matrix.getAt(i, 0).toString());
            for (int j = 1; j < ySize; j++) {
                stringBuffer.append(" ");
                stringBuffer.append(matrix.getAt(i, j).toString());
            }
            System.out.println(stringBuffer.toString());
        }
    }
}
