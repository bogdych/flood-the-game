package com.summerschool.flood.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameParams implements Serializable {

    private String gameName;
    private GameType gameType;
    private Map<String,Object> params;

}