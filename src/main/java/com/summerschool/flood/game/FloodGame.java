package com.summerschool.flood.game;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.summerschool.flood.game.GameStatus.FINISHED;
import static com.summerschool.flood.game.GameStatus.READY;

@Data
public class FloodGame implements IGame {

    private List<Player> players = new ArrayList<>();
    private long id;
    private Field field;
    private int maxPlayers;
    private GameStatus gameStatus;

    public FloodGame(GameType type, int maxPlayersCount) {
        this.maxPlayers = maxPlayersCount;

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
        players.remove(player);
        if (players.size() == 0) {
            gameStatus = FINISHED;
        }
    }

    @Override
    public boolean matchType(GameParams params) {
        return true;
    }

    @Override
    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers) {
            synchronized (this) {
                if (players.size() < maxPlayers) {
                    players.add(player);
                    if (players.size() == maxPlayers) {
                        gameStatus = READY;
                    }
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
}

