package com.summerschool.flood.server;

import com.summerschool.flood.game.Action;
import com.summerschool.flood.game.GameParams;

import java.io.Serializable;

public interface IGameService {

    /** Finds game by specified player settings */
    void findGame(String playerID, GameParams params);

    /** Called when player wants to reconnect to his prev game session, after he was disconnected */
    boolean reconnect(String playerID, String playerNickname) throws ServiceException;

    /** Finds game, makes game actions, validates game logic [internal] */
    void process(String playerID, Action action) throws ServiceException;

    /** Called when session [WS] is closed */
    void disconnect(String playerID) throws ServiceException;

    /** Supposed to be serialized */
    Serializable getPlayerInfo(String playerID) throws ServiceException;

    /** Supposed to be serialized */
    Serializable getGameState(String playerID) throws ServiceException;

    /** Supposed to be serialized */
    Serializable getGameStatus(String playerID) throws ServiceException;

}
