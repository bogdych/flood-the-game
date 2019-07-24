package com.summerschool.flood.game;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class FloodGame implements IGame {

    private List<Player> players = new CopyOnWriteArrayList<>();
    private Field field;
    private int maxPlayersCount;

    public FloodGame(GameType type, int maxPlayersCount) {
        this.maxPlayersCount = maxPlayersCount;

        switch (type) {
            case STANDARD:
                this.field = new Field(10, 10);
                return;

            case FAST:
                this.field = new Field(5, 5);
        }
    }

    @Override
    public void removePlayer(Player player) {
        // todo: remove player properly for game logic
    }

    @Override
    public boolean matchType(GameParams params) {
        return false;
    }

    @Override
    public boolean canAddPLayer() {
        return false;
    }

    @Override
    public boolean addPlayer(Player player) {
        return false;
    }

    @Override
    public Result makeAction(Player player, GameAction action) {
        // todo: run game logic here
        return null;
    }

    @Override
    public Serializable getGameState() {
        return null;
    }

    @Override
    public Serializable getGameStatus() {
        return null;
    }

}

