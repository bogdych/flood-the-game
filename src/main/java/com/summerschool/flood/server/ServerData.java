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

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final List<IGame> games = new CopyOnWriteArrayList<>();
    private final List<Player> players = new CopyOnWriteArrayList<>();

}
