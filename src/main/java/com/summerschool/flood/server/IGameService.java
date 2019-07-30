package com.summerschool.flood.server;

import com.summerschool.flood.message.GameActionMessage;
import com.summerschool.flood.message.FindGameMessage;
import com.summerschool.flood.game.IGame;

public interface IGameService {

    /** Connect player - called first time, when connection is set up */
    void connect(String playerID) throws ServiceException;

    /** Finds game by specified player settings */
    IGame findGame(String playerID, FindGameMessage params) throws ServiceException;

    /** Finds game, makes game actions, validates game logic [internal] */
    void process(String playerID, GameActionMessage action) throws ServiceException;

    /** Called when session [WS] is closed */
    void disconnect(String playerID) throws ServiceException;
}
