package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum  ResultType {

    @JsonProperty("actionProcessed")
    ACTION_PROCESSED,

    @JsonProperty("gameFinished")
    GAME_FINISHED


}
