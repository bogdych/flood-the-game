package com.summerschool.flood.game;

import com.summerschool.flood.game.flood.Field;

public interface GameState {

    /** @return Winner, if the game is finished, or null otherwise */
    Player getWinner();

    /** @return Current game status: to continue of finish the game*/
    GameStatus getGameStatus();

    /** */
    Field getField();
}
