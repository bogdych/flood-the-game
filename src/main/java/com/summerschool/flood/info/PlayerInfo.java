package com.summerschool.flood.info;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.summerschool.flood.misc.JsonSerializable;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Collect all the available data about player in the current
 * server connection session. When player connected to the server, there will be
 * created empty player profile. When session is closed the data will be lost.
 */
public class PlayerInfo implements JsonSerializable {

    @JsonIgnore
    private final String playerId;
    private String nickname = "Rename me";
    private int score = 0;
    private int gamesPlayed = 0;
    private GameSessionInfo sessionInfo = null;

    public PlayerInfo(String playerId) {
        this.playerId = playerId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @NotNull
    public String getPlayerId() {
        return playerId;
    }

    @NotNull
    @JsonGetter
    public String getNickname() {
        return nickname;
    }

    @JsonGetter
    public int getScore() {
        return score;
    }

    @JsonGetter
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    @Override
    public String serialize() throws IOException {
        return new ObjectMapper()
                .writerFor(PlayerInfo.class)
                .writeValueAsString(this);
    }

}
