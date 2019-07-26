package com.summerschool.flood.server;

import com.summerschool.flood.game.GameAction;
import com.summerschool.flood.game.GameParams;
import com.summerschool.flood.game.IGame;
import com.summerschool.flood.game.PlayerInfo;

import java.io.Serializable;

public interface IGameService {

    /** Connect player - called first time, when connection is set up */
    void connect(String playerID) throws ServiceException;

    /** Setup player info [nickname, etc]*/
    void setPlayerInfo(String playerID, PlayerInfo info) throws ServiceException;

    /** Finds game by specified player settings */
    IGame findGame(String playerID, GameParams params) throws ServiceException;

    /** Called when player wants to reconnect to his prev game session, after he was disconnected */
    boolean reconnect(String playerID, String playerNickname) throws ServiceException;

    /** Finds game, makes game actions, validates game logic [internal] */
    void process(String playerID, GameAction action) throws ServiceException;

    /** Called when session [WS] is closed */
    void disconnect(String playerID) throws ServiceException;

    /** Supposed to be serialized */
    Serializable getPlayerInfo(String playerID) throws ServiceException;

    /** Supposed to be serialized */
    Serializable getGameState(String playerID) throws ServiceException;

    /** Supposed to be serialized */
    Serializable getGameStatus(String playerID) throws ServiceException;

}
