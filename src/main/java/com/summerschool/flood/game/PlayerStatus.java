package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlayerStatus {

    @JsonProperty("Winner")
    WINNER,

    @JsonProperty("Loser")
    LOSER,

    @JsonProperty("inGame")
    IN_GAME

}
