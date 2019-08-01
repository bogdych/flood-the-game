package com.summerschool.flood.game;

import com.summerschool.flood.game.flood.Cell;
import com.summerschool.flood.game.flood.Field;

public interface GameState {

    /** @return Winner, if the game is finished, or null otherwise */
    Player getWinner();

    /** @return Next player supposed to make an action [could be null] */
    Player getNext();

    /** @return Next player cell in the game field [its x,y coords and color] */
    Cell getCell();

    /** @return Current game status: to continue of finish the game*/
    GameStatus getGameStatus();

    /** */
    Field getField();
}
