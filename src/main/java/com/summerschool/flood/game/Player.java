package com.summerschool.flood.game;

import lombok.Data;

@Data
public class Player {

    String id;
    String nickname;
    IGame currentGame = null;

}
