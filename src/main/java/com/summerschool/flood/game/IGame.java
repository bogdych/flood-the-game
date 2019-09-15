package com.summerschool.flood.game;

import com.summerschool.flood.message.FindGameMessage;
import com.summerschool.flood.message.MakeActionMessage;

import java.time.Instant;
import java.util.List;

public interface IGame {

    Instant getCreateTime();

    Instant getUpdateTime();

    String getId();

    void removePlayer(Player player);

    void removePlayers();

    boolean matchType(FindGameMessage findGame);

    boolean addPlayer(Player player);

    void makeAction(Player player, MakeActionMessage action);

    List<Player> getPlayers();

    GameState getState();

    boolean isReady();

    boolean isNotReady();

    boolean isFinished();

    void start();
}