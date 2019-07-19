package com.summerschool.flood.game;

import java.util.List;

public interface IGame {

    Result makeAction(Action action);

    List<Player> getPlayers();

}