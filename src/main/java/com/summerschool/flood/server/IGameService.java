package com.summerschool.flood.server;

import com.summerschool.flood.game.Action;

public interface IGameService {

    void connect(String playerID);

    boolean reconnect(String playerID, String playerNickname);

    void process(String playerID, Action action);

    /** Called when session [WS] is closed */
    void disconnect(String playerID);


}
