package com.summerschool.flood.controller;

import com.summerschool.flood.game.IGame;
import com.summerschool.flood.game.Player;

import com.summerschool.flood.server.ServiceData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private final ServiceData serviceData;

    public GameController(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    @GetMapping("/players")
    public Map<String, Player> getPlayers() {
        return serviceData.getPlayerIDMap();
    }

    @GetMapping("/games")
    public List<IGame> getGames() {
        return serviceData.getGames();
    }

}
