package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameParams {

    @JsonProperty("gameName")
    private GameName gameName;

    @JsonProperty("gameType")
    private GameType gameType;

    private Map<String, Object> params;

}
