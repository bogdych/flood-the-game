package com.summerschool.flood.game;

import com.summerschool.flood.game.flood.Cell;
import com.summerschool.flood.game.flood.Color;

import java.util.List;

public interface IFirstSearch {

    List<Cell> start(int x, int y, Color color, List<Cell> otherPlayers);

}
