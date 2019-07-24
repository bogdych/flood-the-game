package com.summerschool.flood.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {

        /** Sent when there is error on server side of in connection, message format */
        @JsonProperty("error")
        ERROR,

        /** Actual player game step (fill with color, in out case) */
        @JsonProperty("makeStep")
        MAKE_STEP,

        /** Sent when player firstly connected to the server (setup player info: name)*/
        @JsonProperty("connect")
        CONNECT,

        /** Sent when player asks to find new game session */
        @JsonProperty("findGame")
        FIND_GAME,

        /** Get player info request */
        @JsonProperty("getPlayerInfo")
        GET_PLAYER_INFO,

        /** Get game state request */
        @JsonProperty("getGameState")
        GET_GAME_STATE,

        /** Server response to player validate game step */
        @JsonProperty("response")
        RESPONSE,

        /** Sent by sever when: someone left the game session, game ended: win, lost */
        @JsonProperty("gameStatus")
        GAME_STATUS

}
