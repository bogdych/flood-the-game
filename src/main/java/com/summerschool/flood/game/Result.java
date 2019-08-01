package com.summerschool.flood.game;

import lombok.Data;

@Data
public class Result {

    private final ResultType type;
    private final Player nextPlayer;
    private final Field field;

}
