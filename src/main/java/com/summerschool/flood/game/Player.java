package com.summerschool.flood.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Player implements Serializable {

    String id;
    String nickname;
    IGame activeGame;
}
