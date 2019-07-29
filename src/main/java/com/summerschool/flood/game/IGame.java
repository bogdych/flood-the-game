package com.summerschool.flood.game;

import com.summerschool.flood.message.GameActionMessage;

import java.util.List;
import java.util.Map;

public interface IGame {

    long getId();

    void removePlayer(Player player);

    boolean matchType(Map<String, String> params);

    boolean addPlayer(Player player);

    Result makeAction(Player player, GameActionMessage action);

    List<Player> getPlayers();

    GameStatus getGameStatus();
}