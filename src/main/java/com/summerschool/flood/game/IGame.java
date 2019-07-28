package com.summerschool.flood.game;

import java.util.List;

public interface IGame {

    long getId();

    void removePlayer(Player player);

    boolean matchType(GameParams params);

    boolean addPlayer(Player player);

    Result makeAction(Player player, GameAction action);

    List<Player> getPlayers();

    GameStatus getGameStatus();
}