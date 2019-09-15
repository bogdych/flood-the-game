package com.summerschool.flood.game.flood;

import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

@Data
public class Cell {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final int COLORS_COUNT = Color.values().length;

    private int x;
    private int y;
    private Color color;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = Color.values()[RANDOM.nextInt(COLORS_COUNT)];
    }

}
