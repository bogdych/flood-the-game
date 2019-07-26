package com.summerschool.flood.server;

import com.summerschool.flood.game.IGame;
import com.summerschool.flood.game.Player;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Component
public class ServiceData {

    /** Mapping via playerID only */
    private final Map<String, Player> playerIDMap = new ConcurrentHashMap<>();

    /** Map playerID and current active game session */
    private final Map<String, IGame> playerIDGameMap = new ConcurrentHashMap<>();

    /** All active game sessions */
    private final List<IGame> games = new CopyOnWriteArrayList<>();

}
