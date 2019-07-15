package com.summerschool.flood.field;



interface Array2D<T> {
     int getXSize();
     int getYSize();
     T getAt(int i, int j);
     void setAt(int i, int j, T val);
     Boolean isInternalCell(int i, int j);
}
