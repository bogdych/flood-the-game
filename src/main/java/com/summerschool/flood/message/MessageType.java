package com.summerschool.flood.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {

        /** Sent when there is error on server side of in connection, message format */
        @JsonProperty("error")
        ERROR,

        /** Actual player game step (fill with color, in out case) */
        @JsonProperty("makeAction")
        MAKE_ACTION,

        /** Sent when player asks to find new game session */
        @JsonProperty("findGame")
        FIND_GAME,

        @JsonProperty("gameReady")
        GAME_READY,

        @JsonProperty("turnEnd")
        TURN_END,
}
