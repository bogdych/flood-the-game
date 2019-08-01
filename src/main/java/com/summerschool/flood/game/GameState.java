package com.summerschool.flood.game;

import com.summerschool.flood.game.flood.Field;

public interface GameState {

    /** @return Current game status: to continue of finish the game*/
    GameStatus getGameStatus();

    /** */
    Field getField();
}
