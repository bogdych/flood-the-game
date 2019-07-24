package com.summerschool.flood.server;

import com.summerschool.flood.game.IGame;
import com.summerschool.flood.game.Player;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Component
public class ServerData {

    /** Connection session for WS game handler */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /** Mapping via playerID only */
    private final Map<String, Player> playerIDMap = new ConcurrentHashMap<>();

    /** Mapping via player nickname for future reconnect feature */
    private final Map<String, Player> playerMap = new ConcurrentHashMap<>();

    /** All active game sessions */
    private final List<IGame> games = new CopyOnWriteArrayList<>();

}
