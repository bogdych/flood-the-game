package com.summerschool.flood.controller;

import com.summerschool.flood.game.Player;
import com.summerschool.flood.server.ServerData;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GameController {

    private final ServerData serverData;

    public GameController(ServerData serverData) {
        this.serverData = serverData;
    }

    @GetMapping("/players")
    public Map<String, Player> getPlayers() {
        System.out.println("/players");
        return serverData.getPlayerMap();
    }

}
