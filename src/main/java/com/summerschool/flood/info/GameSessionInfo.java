package com.summerschool.flood.info;

import com.summerschool.flood.misc.JsonSerializable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Session info about current game between N players. Contents all the
 * data about player, game state, map, input? history.
 */
public class GameSessionInfo implements JsonSerializable {

    private final List<PlayerInfo> sessionPlayers;
    /* And some important info */

    public GameSessionInfo(PlayerInfo ... players) {
        this.sessionPlayers = new CopyOnWriteArrayList<>(players);
    }

    public List<PlayerInfo> getSessionPlayers() {
        return sessionPlayers;
    }

    @Override
    public String serialize() throws IOException {
        return "";
    }

}
