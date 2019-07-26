package com.summerschool.flood.controller;

import com.summerschool.flood.game.IGame;

import com.summerschool.flood.server.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public List<IGame> getGames() {
        return gameService.getGames();
    }

}
