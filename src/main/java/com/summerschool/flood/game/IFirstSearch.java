package com.summerschool.flood.game;

import com.summerschool.flood.game.flood.Color;

public interface IFirstSearch {

    /**
     * Recursive field travers
     * @param x Pos to start
     * @param y Pos to start
     * @param color Fill color
     */
    void start(int x, int y, Color color);

}
