package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameStatus {

    /** Wait for players */
    @JsonProperty("notReady")
    NOT_READY,

    /** When all players were added, but game was not started yet */
    @JsonProperty("ready")
    READY,

    /** Game is finished, has a winner/or no winner */
    @JsonProperty("finished")
    FINISHED

}
