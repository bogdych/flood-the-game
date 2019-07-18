package com.summerschool.flood.controller;

import com.summerschool.flood.game.Player;
import com.summerschool.flood.server.ServerData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    @Autowired
    private ServerData handler;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return handler.getPlayers();
    }

}
