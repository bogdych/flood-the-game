package com.summerschool.flood.game;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class FloodGame implements IGame {

    private List<Player> players = new ArrayList<>();
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
        return true;
    }

    @Override
    public boolean hasPlace() {
        return players.size() < 4;
    }

    @Override
    public boolean addPlayer(Player player) {
        if (hasPlace()) {
            synchronized (this) {
                if (hasPlace()) {
                    players.add(player);
                    return true;
                }
            }
        }
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

    @Override
    public boolean isReady() {
        return players.size() == 4;
    }
}

