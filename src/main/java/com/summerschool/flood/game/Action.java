package com.summerschool.flood.game;

import lombok.Data;

import java.util.Map;

@Data
public class Action {

    private ActionType type;
    private Map<String, Object> params;

}
