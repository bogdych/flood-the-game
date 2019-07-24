package com.summerschool.flood.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PlayerInfo implements Serializable {

    String nickname;

}
