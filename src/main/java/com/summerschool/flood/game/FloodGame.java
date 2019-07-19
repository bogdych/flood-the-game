package com.summerschool.flood.game;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@Data
public class FloodGame implements IGame {

    private List<Player> players = new CopyOnWriteArrayList<>();
    private Field field;
    private @Getter Map<Player, Cell> playersStartPosition = new ConcurrentHashMap<>();
    public FloodGame(GameType type) {

        switch (type) {
            case STANDARD:
                this.field = new Field(10, 10);
                return;

            case FAST:
                this.field = new Field(5, 5);
        }

    }

    @Override
    public Result makeAction(Action action) {
        return null;
    }

}

