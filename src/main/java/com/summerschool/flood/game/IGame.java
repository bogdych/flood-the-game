package com.summerschool.flood.game;

import com.summerschool.flood.message.FindGameMessage;
import com.summerschool.flood.message.MakeActionMessage;

import java.time.Instant;
import java.util.List;

public interface IGame {

    Instant getCreationTime();

    Instant getLastPlayerTime();

    String getId();

    void removePlayer(Player player);

    boolean matchType(FindGameMessage findGame);

    boolean addPlayer(Player player);

    boolean run(Instant now, int waitTime);

    void makeAction(Player player, MakeActionMessage action);

    List<Player> getPlayers();

    GameState getState();

    boolean isReady();

    boolean isFinished();
}