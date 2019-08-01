package com.summerschool.flood.message;

import com.summerschool.flood.game.GameState;
import lombok.Data;

@Data
public class GameStateMessage extends Message {

    private GameState state;

    public GameStateMessage(GameState state, MessageType type) {
        this.state = state;
        this.type = type;
    }
}
