package com.summerschool.flood.gameSquare;

import lombok.Getter;

import java.util.ArrayList;

public class Matrix2D<T> implements Array2D<T> {

    private @Getter int XSize;
    private @Getter int YSize;
    private ArrayList<ArrayList<T>> matrix;

    public Matrix2D(int iSize, int jSize, T def){
        matrix = new ArrayList<>();
        XSize = iSize;
        YSize = jSize;
        for(int i = 0; i < iSize; i++){
            matrix.add(new ArrayList<>());
            for(int j = 0; j < jSize; j++)
                matrix.get(i).add(def);

        }
    }
    public Boolean IsInternalCell(int xCell, int yCell){
        if(xCell < 0 || XSize <= xCell ||
                yCell < 0 || YSize <= yCell)
            return false;
        return true;
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
