package com.summerschool.flood.message;

import com.summerschool.flood.game.State;
import lombok.Data;

@Data
public class GameStateMessage extends Message {

    private State state;

    public GameStateMessage(State state, MessageType type) {
        this.state = state;
        this.type = type;
    }
}
