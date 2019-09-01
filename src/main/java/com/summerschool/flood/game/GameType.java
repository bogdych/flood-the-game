package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameType {

    @JsonProperty("fast")
    FAST,

    @JsonProperty("standard")
    STANDARD,

}
