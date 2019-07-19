package com.summerschool.flood.game;

import java.util.List;

public interface IGame {

    void removePlayer(Player player);

    boolean addPlayer(Player player);

    Result makeAction(Player player, Action action);

    List<Player> getPlayers();

}