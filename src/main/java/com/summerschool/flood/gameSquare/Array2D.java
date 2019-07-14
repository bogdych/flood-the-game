package com.summerschool.flood.gameSquare;



interface Array2D<T> {
     int getXSize();
     int getYSize();
     T getAt(int i, int j);
     void setAt(int i, int j, T val);
     Boolean IsInternalCell(int i, int j);
}
