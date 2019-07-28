package com.summerschool.flood.server;

import com.summerschool.flood.game.GameAction;
import com.summerschool.flood.game.GameParams;
import com.summerschool.flood.game.IGame;

public interface IGameService {

    /** Connect player - called first time, when connection is set up */
    void connect(String playerID) throws ServiceException;

    /** Finds game by specified player settings */
    IGame findGame(String playerID, GameParams params) throws ServiceException;

    /** Finds game, makes game actions, validates game logic [internal] */
    void process(String playerID, GameAction action) throws ServiceException;

    /** Called when session [WS] is closed */
    void disconnect(String playerID) throws ServiceException;
}
