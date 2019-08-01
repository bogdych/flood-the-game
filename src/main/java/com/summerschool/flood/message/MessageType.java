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

        /** Sent when game session ready to be begun (has all the players) */
        @JsonProperty("gameReady")
        GAME_READY,

        /** Server send this message in time of active game session */
        @JsonProperty("gameState")
        GAME_STATE,


}
