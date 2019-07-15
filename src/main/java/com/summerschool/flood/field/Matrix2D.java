package com.summerschool.flood.field;

import lombok.Getter;

import java.util.ArrayList;

public class Matrix2D<T> implements Array2D<T> {

    private @Getter int xSize;
    private @Getter int ySize;
    private ArrayList<ArrayList<T>> matrix;

    public Matrix2D(int iSize, int jSize, T def){
        matrix = new ArrayList<>();
        xSize = iSize;
        ySize = jSize;
        for(int i = 0; i < iSize; i++){
            matrix.add(new ArrayList<>());
            for(int j = 0; j < jSize; j++)
                matrix.get(i).add(def);

        }
    }
    public Boolean isInternalCell(int xCell, int yCell){
        return !(xCell < 0 || xSize <= xCell ||
                 yCell < 0 || ySize <= yCell);
    }
    public T getAt(int i, int j){
        return matrix.get(i).get(j);
    }
    public T getAt(Pair<Integer, Integer> pair){
        return getAt(pair.getFirst(), pair.getSecond());
    }
    public void setAt(int i, int j, T val){
        matrix.get(i).set(j, val);
    }
    public void setAt(Pair<Integer, Integer> pair, T value){
        setAt(pair.getFirst(), pair.getSecond(), value);
    }
}
