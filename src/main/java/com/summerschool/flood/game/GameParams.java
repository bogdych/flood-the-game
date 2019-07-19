package com.summerschool.flood.game;

import lombok.Data;

import java.util.Map;

@Data
public class GameParams {
    String gameName;
    GameType gameType;
    private Map<String,Object> params;
}
