package com.summerschool.flood.game;

import com.summerschool.flood.message.MakeActionMessage;

import java.util.List;
import java.util.Map;

public interface IGame {

    String getId();

    void removePlayer(Player player);

    boolean matchType(Map<String, String> params);

    boolean addPlayer(Player player);

    void makeAction(Player player, MakeActionMessage action);

    List<Player> getPlayers();

    State getState();

    boolean isReady();

    boolean isFinished();
}