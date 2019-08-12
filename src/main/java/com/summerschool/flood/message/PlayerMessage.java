package com.summerschool.flood.message;

import com.summerschool.flood.game.Player;
import lombok.Data;

@Data
public class PlayerMessage extends Message {

    private Player player;

    public PlayerMessage(Player player) {
        this.player = player;
        this.type = MessageType.PLAYER_INFO;
    }

}
