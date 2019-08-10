package com.summerschool.flood.game;

import com.summerschool.flood.game.flood.Cell;
import lombok.Data;

/**
 * Stores single player current position on the field.
 * All the players positions are stored in th game state.
 */
@Data
public class PlayerPosition {

    private String id;
    private Cell cell;

    public PlayerPosition(Player player, Cell cell) {
        this.id = player.getId();
        this.cell = cell;
    }

}
