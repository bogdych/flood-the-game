package com.summerschool.flood.game;

import java.io.Serializable;
import java.util.List;

public interface IGame {

    void removePlayer(Player player);

    boolean matchType(GameParams params);

    boolean hasPlace();

    boolean addPlayer(Player player);

    Result makeAction(Player player, GameAction action);

    List<Player> getPlayers();

    Serializable getGameState();

    Serializable getGameStatus();

    boolean isReady();

}