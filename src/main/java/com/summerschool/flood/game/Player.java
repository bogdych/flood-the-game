package com.summerschool.flood.game;

import lombok.Data;

import java.io.Serializable;

@Data
public class Player implements Serializable {

    String id;
    String nickname;
    IGame activeGame;
}
