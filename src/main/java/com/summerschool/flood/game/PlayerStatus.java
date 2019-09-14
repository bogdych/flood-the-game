package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlayerStatus {

    @JsonProperty("winner")
    WINNER,

    @JsonProperty("loser")
    LOSER,

    @JsonProperty("inGame")
    IN_GAME

}
